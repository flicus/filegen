package org.schors.filegen.parameters;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.schors.filegen.TParameter;

import java.util.UUID;

public class GUIDNumber extends TParameter {

    private TextField name = new TextField("Name");

    public GUIDNumber() {
        name.setWidth(100, Sizeable.Unit.PERCENTAGE);
    }

    @Override
    public Component getOtherGui() {
        return null;
    }

    @Override
    public Component getNameGui() {
        return name;
    }

    @Override
    public String apply(String template) {

        String placeholder = "{{" + name.getValue() + "}}";
        String result = template;
        while (result.contains(placeholder)) {
            result = result.replace(placeholder, String.valueOf(UUID.randomUUID().toString()));
        }
        return result;
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public void reset() {
        //do nothing
    }
}
