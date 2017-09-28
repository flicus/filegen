package org.schors.filegen.parameters;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.schors.filegen.TParameter;

public class IncrementingNumber extends TParameter {

    private TextField start = new TextField("Start");
    private TextField name = new TextField("Name");

    private long current = 0;

    public IncrementingNumber() {
        start.addValueChangeListener(event -> {
            current = Long.valueOf(start.getValue());
        });
    }

    @Override
    public Component getOtherGui() {
//        HorizontalLayout layout = new HorizontalLayout();
//        layout.setWidth(100, Sizeable.Unit.PERCENTAGE);
//        layout.setSpacing(true);
//        layout.addComponent(name);
//        layout.addComponent(start);
//        Button remove = new Button("");
//        remove.addStyleName("small");
//        layout.addComponent(remove);
//        layout.setComponentAlignment(remove, Alignment.MIDDLE_RIGHT);
        return start;
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
            result = result.replace(placeholder, String.valueOf(current++));
        }
        return result;
    }

    @Override
    public String getName() {
        return name.getValue();
    }
}
