package domain.web.pageobjects.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;

public class TopMenu<T> {
    private T page;
    private final SelenideElement parent = $("#message-wrapper"),
            commandMenu = parent.$("#messages_collapsed"),
            attackAllert = parent.$("#attack_alert"),
            eventContent = $("#eventContent");

    private final ElementsCollection rows = eventContent.$$("tr.eventFleet");

    private List<Integer> findRowWithAttack() {
        commandMenu.click();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            var mission = rows.get(i).$(".missionFleet").$("img").getAttribute("title");
            if (mission.substring(mission.indexOf("|") + 1).trim().equals("Atakuj")) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    public  Map<String, String> getPlanetCoordinates() {
        Map<String,String> coordinates = new HashMap<>();
        findRowWithAttack().forEach(i -> {
            coordinates.put(rows.get(i).$(".destFleet figure").getAttribute("title"), rows.get(i).$(".destCoords").getText());
        });
        return coordinates;
    }




}