package org.schors.filegen;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.schors.filegen.parameters.IncrementingNumber;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import org.vaadin.aceeditor.client.AceDoc;

import java.util.ArrayList;
import java.util.List;

@PreserveOnRefresh
@Theme("rbm")
public class TheApp extends UI {

    AceDoc aceDoc = null;
    private Label templateLabel = new Label("Template");
    private Label notChecked = new Label(new String(Character.toChars(VaadinIcons.QUESTION.getCodepoint())));
    private Label correct = new Label(new String(Character.toChars(VaadinIcons.CHECK_CIRCLE.getCodepoint())));
    private Label incorrect = new Label(new String(Character.toChars(VaadinIcons.CLOSE_CIRCLE.getCodepoint())));
    private Label result = new Label("Result:");
    private Label parseResult = new Label("");
    private TextField count = new TextField("Records count");
    private AceEditor templateEditor;
    private Button validate = new Button("Validate");
    private Button generate = new Button("Generate");
    private List<TParameter> data = new ArrayList<>();
    private Grid<TParameter> grid = new Grid();

    @Override
    protected void init(VaadinRequest request) {

        notChecked.addStyleName("grey");
        correct.addStyleName("green");
        correct.setVisible(false);
        incorrect.addStyleName("red");
        incorrect.setVisible(false);

        generate.setIcon(VaadinIcons.BOLT);
        generate.setEnabled(false);

        validate.setIcon(VaadinIcons.SEARCH);
        validate.addClickListener(event -> {
            final String[] tpl = {new String("" + templateEditor.getValue())};
            data.stream().forEach(parameter -> {
                tpl[0] = parameter.apply(tpl[0]);
            });
            parseResult.setValue(tpl[0]);
            if (tpl[0].contains("{{") || tpl[0].contains("}}")) {
                notChecked.setVisible(false);
                incorrect.setVisible(true);
                correct.setVisible(false);
                generate.setEnabled(false);
            } else {
                notChecked.setVisible(false);
                incorrect.setVisible(false);
                correct.setVisible(true);
                generate.setEnabled(true);
            }
        });

        VerticalLayout left = new VerticalLayout();
        left.setSpacing(true);
        left.setMargin(new MarginInfo(false, true, false, false));
        left.setWidth(100, Unit.PERCENTAGE);

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth(100, Unit.PERCENTAGE);
        top.addComponent(templateLabel);

        top.addComponent(notChecked);
        top.setComponentAlignment(notChecked, Alignment.MIDDLE_RIGHT);
        top.addComponent(correct);
        top.setComponentAlignment(correct, Alignment.MIDDLE_RIGHT);
        top.addComponent(incorrect);
        top.setComponentAlignment(incorrect, Alignment.MIDDLE_RIGHT);

        left.addComponent(top);

        templateEditor = new AceEditor();
        templateEditor.setMode(AceMode.handlebars);
        templateEditor.setTheme(AceTheme.dawn);
        templateEditor.setWidth(100, Unit.PERCENTAGE);
        templateEditor.setHeight(5, Unit.EM);
        templateEditor.addValueChangeListener(event -> {
            notChecked.setVisible(true);
            correct.setVisible(false);
            incorrect.setVisible(false);
            generate.setEnabled(false);
            parseResult.setValue("");
        });
        left.addComponent(templateEditor);

        HorizontalLayout resultToolbar = new HorizontalLayout();
        resultToolbar.setSpacing(true);
        resultToolbar.addComponent(result);
        resultToolbar.addComponent(parseResult);
        left.addComponent(resultToolbar);

        HorizontalLayout leftToolbar = new HorizontalLayout();
        leftToolbar.setMargin(false);
        leftToolbar.setSpacing(true);
        leftToolbar.addComponent(validate);
        leftToolbar.addComponent(generate);
        left.addComponent(leftToolbar);

        HorizontalLayout main = new HorizontalLayout();
        main.setSizeFull();
        main.setSpacing(false);
        main.setMargin(true);

        HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.setSizeFull();
        split.setSplitPosition(35, Unit.PERCENTAGE);
        split.addComponent(left);

        main.addComponent(split);
        main.setExpandRatio(split, 1);

        HorizontalLayout right = new HorizontalLayout();
        right.setSpacing(true);

        VerticalLayout exist = new VerticalLayout();
        exist.setSpacing(true);
        exist.setWidthUndefined();
        exist.setHeight(100, Unit.PERCENTAGE);

        Button incNumber = new Button("Number");
        incNumber.addStyleName("primary");
        DragSourceExtension<Button> source = new DragSourceExtension<>(incNumber);
        source.setEffectAllowed(EffectAllowed.COPY);
        source.setDragData(IncrementingNumber.class);
        exist.addComponent(incNumber);

        grid.setSizeFull();
        grid.setItems(data);
        grid.addComponentColumn(param -> param.getNameGui()).setCaption("Name");
        grid.addComponentColumn(param -> param.getOtherGui());
        grid.addColumn(param -> "Delete",
                new ButtonRenderer<>(event -> {
                    data.remove(event.getItem());
                    grid.setItems(data);
                }));
        grid.setSelectionMode(Grid.SelectionMode.NONE);


        DropTargetExtension<Grid> target = new DropTargetExtension<>(grid);
        target.setDropEffect(DropEffect.COPY);
        target.addDropListener(event -> {
            event.getDragData().ifPresent(o -> {
                Class clazz = (Class) o;
                TParameter p = null;
                try {
                    p = (TParameter) clazz.newInstance();
                    data.add(p);
                    grid.setItems(data);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });


        right.addComponent(grid);
        right.setExpandRatio(grid, 1);
        right.addComponent(exist);
//        right.setExpandRatio(exist, 1);

        split.addComponent(right);

        setContent(main);
    }
}
