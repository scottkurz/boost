package io.openliberty.boost;

import org.w3c.dom.Document;
import io.openliberty.boost.BoosterPackConfigurator;

public class JAXRSBoosterPackConfigurator implements BoosterPackConfigurator {
		
	String featureString = null;
	
	@Override
	public String getFeatureString() {
		return featureString;
	}

	@Override
	public void writeConfigToServerXML(Document doc) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setFeatureString(String featureStr) {
		featureString = featureStr;
	}
}
