/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package boost.runtimes.tomee.boosters;

import java.util.List;
import java.util.Map;

import boost.common.BoostException;
import boost.common.BoostLoggerI;
import boost.common.boosters.MPJWTBoosterConfig;
import boost.common.config.BoosterConfigParams;

public class TomeeMPJWTBoosterConfig extends MPJWTBoosterConfig {

    public TomeeMPJWTBoosterConfig(BoosterConfigParams params, BoostLoggerI logger) throws BoostException {
        super(params, logger);
    }

    @Override
    public List<String> getDependencies() {
        List<String> deps = super.getDependencies();
        deps.add("org.eclipse.microprofile.jwt:microprofile-jwt-auth-api:1.1");
        deps.add("org.apache.geronimo:geronimo-jwt-auth:1.0.3");
        deps.add("org.apache.geronimo:geronimo-security:1.0");
        return deps;
    }
}
