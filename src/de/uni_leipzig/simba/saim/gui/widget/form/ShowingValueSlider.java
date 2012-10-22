package de.uni_leipzig.simba.saim.gui.widget.form;

import java.text.NumberFormat;
import java.util.Collection;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
/**
 * Custom component basically wraps around Slider and adds an Label showing the current value.
 * @TODO use FieldWrapper class from the CustomField add-on to avoid 80% of code.
 * @see https://vaadin.com/forum/-/message_boards/view_message/294045
 */
public class ShowingValueSlider extends CustomComponent implements Field {

	private static final long serialVersionUID = 3590831032966822810L;

	Slider slider = new Slider();

	private Label value = new Label("0");

    public ShowingValueSlider(String caption, double min, double max, int resolution) {
        // Initilization
        setCaption(caption);
        slider.setMin(min);
        slider.setMax(max);
        slider.setResolution(resolution);
        slider.setWidth("100%");
        slider.setImmediate(true);

        value.setWidth(null);
        slider.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = -860672027897366148L;

            @Override
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                value.setValue(NumberFormat.getInstance().format(event.getProperty().getValue()));
            }
        });

        // Component root
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setWidth("100%");
        hl.addComponent(slider);
        hl.setComponentAlignment(slider, Alignment.MIDDLE_CENTER);
        hl.setExpandRatio(slider, 1);
        hl.addComponent(value);
        hl.setComponentAlignment(value, Alignment.MIDDLE_LEFT);

        setCompositionRoot(hl);
    }



    @Override
    public void focus() {
        slider.focus();
    }

    @Override
    public String getRequiredError() {
        return slider.getRequiredError();
    }

    @Override
    public boolean isRequired() {
        return slider.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        slider.setRequired(required);
    }

    @Override
    public void setRequiredError(String requiredMessage) {
        slider.setRequiredError(requiredMessage);
    }

    @Override
    public boolean isInvalidCommitted() {
        return slider.isInvalidCommitted();
    }

    @Override
    public void setInvalidCommitted(boolean isCommitted) {
        slider.setInvalidCommitted(isCommitted);
    }

    @Override
    public void commit() throws SourceException, InvalidValueException {
        slider.commit();
    }

    @Override
    public void discard() throws SourceException {
        slider.discard();
    }

    @Override
    public boolean isModified() {
        return slider.isModified();
    }

    @Override
    public boolean isReadThrough() {
        return slider.isReadThrough();
    }

    @Override
    public boolean isWriteThrough() {
        return slider.isWriteThrough();
    }

    @Override
    public void setReadThrough(boolean readThrough) throws SourceException {
        slider.setReadThrough(readThrough);
    }

    @Override
    public void setWriteThrough(boolean writeThrough) throws SourceException,
            InvalidValueException {
        slider.setWriteThrough(writeThrough);
    }

    @Override
    public void addValidator(Validator validator) {
        slider.addValidator(validator);
    }

    @Override
    public Collection<Validator> getValidators() {
        return slider.getValidators();
    }

    @Override
    public boolean isInvalidAllowed() {
        return slider.isInvalidAllowed();
    }

    @Override
    public boolean isValid() {
        return slider.isValid();
    }

    @Override
    public void removeValidator(Validator validator) {
        slider.removeValidator(validator);
    }

    @Override
    public void setInvalidAllowed(boolean invalidValueAllowed)
            throws UnsupportedOperationException {
        slider.setInvalidAllowed(invalidValueAllowed);
    }

    @Override
    public void validate() throws InvalidValueException {
        slider.validate();
    }

    @Override
    public Class<?> getType() {
        return slider.getType();
    }

    @Override
    public Object getValue() {
        return slider.getValue();
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        slider.setValue(newValue);
    }

    @Override
    public void addListener(ValueChangeListener listener) {
        slider.addListener(listener);
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
        slider.removeListener(listener);
    }

    @Override
    public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
        slider.valueChange(event);
    }

    @Override
    public Property getPropertyDataSource() {
        return slider.getPropertyDataSource();
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        slider.setPropertyDataSource(newDataSource);
    }

    @Override
    public int getTabIndex() {
        return slider.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        slider.setTabIndex(tabIndex);
    }
}
