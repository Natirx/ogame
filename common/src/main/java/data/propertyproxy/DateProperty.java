package data.propertyproxy;

import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@ToString
public class DateProperty implements GeneratedProperty<LocalDate>, Serializable {

    private static final long serialVersionUID = 5300119055129818624L;
    private LocalDate date;

    public DateProperty(LocalDate date) {
        this.date = date;
    }

    public LocalDate getValue() {
        return date;
    }
}