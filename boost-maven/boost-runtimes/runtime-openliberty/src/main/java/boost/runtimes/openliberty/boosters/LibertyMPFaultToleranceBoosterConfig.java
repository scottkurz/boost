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
package boost.runtimes.openliberty.boosters;

import static boost.common.config.ConfigConstants.*;

import boost.common.BoostException;
import boost.common.BoostLoggerI;
import boost.common.boosters.MPFaultToleranceBoosterConfig;
import boost.common.config.BoosterConfigParams;
import boost.runtimes.openliberty.LibertyServerConfigGenerator;
import boost.runtimes.openliberty.boosters.LibertyBoosterI;

public class LibertyMPFaultToleranceBoosterConfig extends MPFaultToleranceBoosterConfig implements LibertyBoosterI {

    public LibertyMPFaultToleranceBoosterConfig(BoosterConfigParams params, BoostLoggerI logger) throws BoostException {
        super(params, logger);
    }

    @Override
    public String getFeature() {
        if (getVersion().equals(MP_FAULTTOLERANCE_VERSION_11)) {
            return MPFAULTTOLERANCE_11;
        } else if (getVersion().equals(MP_FAULTTOLERANCE_VERSION_20)) {
            return MPFAULTTOLERANCE_20;
        }

        return null;
    }

    @Override
    public void addServerConfig(LibertyServerConfigGenerator libertyServerConfigGenerator) {

    }
}
