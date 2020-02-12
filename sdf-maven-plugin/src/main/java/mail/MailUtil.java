package mail;

import utils.WaitUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class MailUtil {
    /*
     * get first 30 emails to keep performance in shape
     * */
    private final static int NUMBER_OF_SEARCHED_EMAILS = 30;
    private static ThreadLocal<Store> stores = new ThreadLocal<>();
    private List<Folder> folders = new ArrayList<>();
    private MailConnectivityModel connectEntity;

    public MailUtil(EmailAccountModel account) {
        this.connectEntity = MailConnectivityBuilder.build(account);
    }

    public String getMessageContent(Predicate<String> subject, Duration duration, LocalDateTime time) {
        AtomicReference<String> body = new AtomicReference<>();
        WaitUtils.until(duration, Duration.ofSeconds(10), () -> {
            try {
                connect(Folder.READ_ONLY);
                List<Message> collect = getMessages(time).stream().filter(m -> {
                    boolean r;
                    try {
                        r = subject.test(m.getSubject());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Couldn't read message");
                    }
                    return r;
                }).collect(Collectors.toList());
                if (collect.isEmpty()) {
                    throw new RuntimeException("Email hasn't come");
                }
                try {
                    body.set(getMessageTextContent(collect.get(0)));
                } catch (IOException | MessagingException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Couldn't read mail content");
                }
            } finally {
                close();
            }
        });

        return body.get();
    }

    private String getMessageTextContent(Message msg) throws IOException, MessagingException {
        String result;
        ContentType contentType = new ContentType(msg.getContentType());
        if (contentType.getPrimaryType().equals("multipart")) {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(msg.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            result = sb.toString();
        } else {
            result = msg.getContent().toString();
        }
        return result;
    }

    /**
     * Get messages that came after
     *
     * @param time
     */
    private Collection<Message> getMessages(LocalDateTime time) {
        ArrayList<Message> messages = new ArrayList<>();
        for (Folder f : folders) {
            try {
                int count = f.getMessageCount();
                if (count != 0) {
                    if (count < NUMBER_OF_SEARCHED_EMAILS) {
                        Collections.addAll(messages, f.getMessages(1, count));
                    } else {
                        Collections.addAll(messages, f.getMessages(count - NUMBER_OF_SEARCHED_EMAILS, count));
                    }
                }
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new RuntimeException("Couldn't obtain messages");
            }
        }
        return messages.stream().filter(s -> {
            boolean r;
            try {
                r = s.getReceivedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(time);
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new RuntimeException("Couldn't obtain message received date");
            }
            return r;
        }).collect(Collectors.toList());
    }

    private void connect(int access) {
        Properties props = new Properties();
        props.setProperty(connectEntity.getProtocolSysName(), connectEntity.getProtocolSysValue());
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect(
                    connectEntity.getProtocol(),
                    connectEntity.getEmailAccount().getUsername(),
                    connectEntity.getEmailAccount().getPassword()
            );
            stores.set(store);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't connect to " + connectEntity.getEmailAccount().getUsername());
        }
        openFolders(access);
    }

    private void close() {
        try {
            if (stores.get() != null) {
                stores.get().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't close connection of " + connectEntity.getEmailAccount().getUsername());
        } finally {
            stores.remove();
        }
    }

    /**
     * As an access parameter please use {@link Folder#READ_ONLY} or {@link Folder#READ_WRITE}
     */
    private void openFolders(int access) {
        folders.clear();
        for (String folder : connectEntity.getFolderNames()) {
            try {
                Folder f = stores.get().getFolder(folder);
                f.open(access);
                folders.add(f);
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new RuntimeException("Couldn't open folder" + folder);
            }
        }
    }

    public void send(List<String> recipients, String subject, String text) {
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(connectEntity.getEmailAccount().getUsername(), connectEntity.getEmailAccount().getPassword());
            }
        };
        Session session = Session.getInstance(connectEntity.getSendingProperties(), authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(connectEntity.getEmailAccount().getUsername()));
            message.setRecipients(Message.RecipientType.TO, recipients.stream().map(address -> {
                try {
                    return new InternetAddress(address);
                } catch (AddressException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList()).toArray(new Address[recipients.size()]));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(List<String> recipients, String subject, String text, String mimeText, Path attachment) {
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(connectEntity.getEmailAccount().getUsername(), connectEntity.getEmailAccount().getPassword());
            }
        };
        Session session = Session.getInstance(connectEntity.getSendingProperties(), authenticator);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(connectEntity.getEmailAccount().getUsername()));
            message.setRecipients(Message.RecipientType.TO, recipients.stream().map(address -> {
                try {
                    return new InternetAddress(address);
                } catch (AddressException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList()).toArray(new Address[recipients.size()]));
            message.setSubject(subject);
            message.setText(text);
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(mimeText);
            MimeBodyPart mbp2 = new MimeBodyPart();
            mbp2.attachFile(attachment.toFile());
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            message.setContent(mp);
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
