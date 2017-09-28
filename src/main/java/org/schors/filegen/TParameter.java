package org.schors.filegen;

import com.vaadin.ui.Component;

public abstract class TParameter {
    public abstract Component getOtherGui();

    public abstract Component getNameGui();

    public abstract String apply(String template);

    public abstract String getName();
}
