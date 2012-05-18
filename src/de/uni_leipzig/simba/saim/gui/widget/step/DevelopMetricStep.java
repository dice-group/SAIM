//package de.uni_leipzig.simba.saim.gui.widget.step;
//
//import org.vaadin.teemu.wizards.WizardStep;
//
//import com.vaadin.data.Property.ValueChangeEvent;
//import com.vaadin.data.Property.ValueChangeListener;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.ListSelect;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.Window;
//
//import de.uni_leipzig.simba.saim.Messages;
//import de.uni_leipzig.simba.saim.SAIMApplication;
//
///**
// * Step to decide upon next step: learn or manual configure link specifications.
// * @author Lyko
// * @deprecated
// */
//public class DevelopMetricStep implements WizardStep
//{
//	private final Messages messages;	
//	public DevelopMetricStep(final Messages messages){this.messages=messages;}
//		
//	private final DevelopMetricStep instance;
//	String caption = messages.getString("DevelopMetricStep.caption"); //$NON-NLS-1$
//	Panel content = new Panel(messages.getString("DevelopMetricStep.panel")); //$NON-NLS-1$
//	
//	String learn = messages.getString("DevelopMetricStep.learn"); //$NON-NLS-1$
//	String generate = messages.getString("DevelopMetricStep.manual"); //$NON-NLS-1$
//	
//	@Override
//	public String getCaption() {
//		return caption;
//	}
//
//	@Override
//	public Component getContent() {
//		/** Need a window to ask user about next step  **/
//		final Window sub = new Window(messages.getString("DevelopMetricStep.subwindowcaption")); //$NON-NLS-1$
//		sub.setWidth("30em");
//		ListSelect selector = new ListSelect(messages.getString("DevelopMetricStep.subwindowselect")); //$NON-NLS-1$
//		selector.addItem(learn);
//		selector.addItem(generate);
//		selector.setNullSelectionAllowed(false);
//		selector.setImmediate(true);
//		selector.addListener(new ValueChangeListener() {			
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				Object o = event.getProperty();
//				System.out.println(o);
//				if(o.toString().equals(learn)) {
//					((SAIMApplication) SAIMApplication.getInstance()).setStep(instance, new LearningStep());
//				} 
//				if(o.toString().equals(generate)) {
//					((SAIMApplication) SAIMApplication.getInstance()).setStep(instance, new MetricStep());
//				}
//				SAIMApplication.getInstance().getMainWindow().removeWindow(sub);
//				
//			}
//		});
//		sub.addComponent(selector);
//		sub.setModal(true);
//		SAIMApplication.getInstance().getMainWindow().addWindow(sub);
//		
//		return content;
//	}
//
//	@Override
//	public boolean onAdvance() {
//		return true;
//	}
//
//	@Override
//	public boolean onBack() {
//		((SAIMApplication) SAIMApplication.getInstance()).removeOlderSteps(this);
//		return true;
//	}
//
//	public void setCaption(String caption) {
//		
//	}
//}
