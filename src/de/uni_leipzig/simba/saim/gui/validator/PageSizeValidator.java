package de.uni_leipzig.simba.saim.gui.validator;

import com.vaadin.data.validator.IntegerValidator;

public class PageSizeValidator extends IntegerValidator
{
	public PageSizeValidator(String errorMessage){super(errorMessage);}

	private static final long	serialVersionUID	= -5470766225738299746L;

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		super.validate(value);
		if(Integer.valueOf(value.toString())<=0 && Integer.valueOf(value.toString()) != -1) throw new InvalidValueException("Page size < 0.");
		
	}

	@Override
	public boolean isValid(Object value)
	{
		return super.isValid(value)&&(Integer.valueOf(value.toString())>0 ||Integer.valueOf(value.toString())==-1) ;
	}
}