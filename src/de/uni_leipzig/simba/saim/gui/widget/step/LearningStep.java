//package de.uni_leipzig.simba.saim.gui.widget.step;
//
//import org.vaadin.teemu.wizards.WizardStep;
//
//import com.vaadin.ui.Component;
//import com.vaadin.ui.Panel;
//
//import de.uni_leipzig.simba.saim.Messages;
//import de.uni_leipzig.simba.saim.gui.widget.panel.LearningPanel;
///**
// * @deprecated
// * @author Lyko
// *
// */
//public class LearningStep implements WizardStep
//{
//	private final Messages messages;	
//	public LearningStep(final Messages messages) {this.messages=messages;}
//	Panel content = new LearningPanel();
//	
//	@Override
//	public String getCaption() {
//		return messages.getString("LearningStep.caption");
//	}
//
//	@Override
//	public Component getContent() {
//		return content;
//	}
//
//	@Override
//	public boolean onAdvance() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean onBack() {
//		return true;
//	}
//
//}
