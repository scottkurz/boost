package io.openliberty.boost;

import java.util.ArrayList;
import java.util.List;

import io.openliberty.boost.BoosterPackConfigurator;

public class BoosterPacksParent {

	/**
	 * Creates a list of config writer objects for all boost dependencies found
	 */
	
	List<String> featureList;
	
	JDBCBoosterPackConfigurator jdbcConfig = null;
	private String JDBC_BOOSTER_PACK_STRING = "liberty-booster-data-jdbc";
	private String JAXRS_BOOSTER_PACK_STRING_EE7 = "io.openliberty.boost:boost-jaxrs:ee7";
	private String JAXRS_BOOSTER_PACK_STRING_EE8 = "io.openliberty.boost:boost-jaxrs:ee8";
	private List<BoosterPackConfigurator> boosterPackConfigList = new ArrayList<BoosterPackConfigurator>();

	/**
	 * take a list of pom boost dependency strings and map to liberty features for config. 
	 * return a list of feature configuration objects for each found dependency.
	 * 
	 * @param dependencies
	 * @return
	 */
	public List<BoosterPackConfigurator> mapDependenciesToFeatureList(List<String> dependencies) {
					
		featureList = new ArrayList<String>();
		for(String dep: dependencies) {
			if (dep.equals(JDBC_BOOSTER_PACK_STRING)){			
				boosterPackConfigList.add(new JDBCBoosterPackConfigurator());
			} else if (dep.equals(JAXRS_BOOSTER_PACK_STRING_EE7) || dep.equals(JAXRS_BOOSTER_PACK_STRING_EE8)){	
				JAXRSBoosterPackConfigurator jaxrsCfg = new JAXRSBoosterPackConfigurator();
				if (dep.equals(JAXRS_BOOSTER_PACK_STRING_EE7)){
					jaxrsCfg.setFeatureString("jaxrs-2.0");
				}
				else {
					jaxrsCfg.setFeatureString("jaxrs-2.1");
				}
				boosterPackConfigList.add(jaxrsCfg);
			}
			
		}
		
		return boosterPackConfigList;
	}

	public void writeConfigForFeature(String feature) {
		
	}
}
