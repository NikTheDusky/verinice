/*******************************************************************************
 * Copyright (c) 2018 Jochen Kemnade.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * Contributors:
 * - Jochen Kemnade
 * - Alexander Ben Nasrallah
 *
 ******************************************************************************/
package sernet.verinice.model.bp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import sernet.verinice.model.bp.elements.BpRequirement;
import sernet.verinice.model.bp.elements.Safeguard;
import sernet.verinice.model.common.CnATreeElement;

/**
 * The security level for basic protection (BP) requirements, safeguards etc.
 */
public enum SecurityLevel {

    BASIC, STANDARD, HIGH;

    private static final Map<String, SecurityLevel> qualifiersByPropertyValue;
    static {
        Map<String, SecurityLevel> m = new HashMap<>();
        m.put(Safeguard.PROP_QUALIFIER_BASIC, BASIC);
        m.put(BpRequirement.PROP_QUALIFIER_BASIC, BASIC);

        m.put(Safeguard.PROP_QUALIFIER_STANDARD, STANDARD);
        m.put(BpRequirement.PROP_QUALIFIER_STANDARD, STANDARD);
        m.put(Safeguard.PROP_QUALIFIER_HIGH, HIGH);
        m.put(BpRequirement.PROP_QUALIFIER_HIGH, HIGH);

        m.put(null, null);
        m.put("", null);

        qualifiersByPropertyValue = Collections.unmodifiableMap(m);
    }

    private String label;

    private SecurityLevel() {
        label = Messages.getString(getClass().getSimpleName() + "." + this.name());
    }

    public String getLabel() {
        return label;
    }

    /**
     * Find the qualifier of an element. If the given element does not support a
     * qualifier or if the qualifier is an unknown/custom value, the method will
     * return <code>null</code>. If the element supports but does not have a
     * qualifier, the method will return {@link SecurityLevel#PRISTINE}.
     *
     * @param element
     *            the element
     * @return the appropriate qualifier or <code>null</code> for unsupported
     *         element types
     */

    public static SecurityLevel findValue(CnATreeElement element) {
        String qualifier;

        if (element instanceof Safeguard) {
            qualifier = ((Safeguard) element).getEntity()
                    .getRawPropertyValue(Safeguard.PROP_QUALIFIER);
        } else if (element instanceof BpRequirement) {
            qualifier = ((BpRequirement) element).getEntity()
                    .getRawPropertyValue(BpRequirement.PROP_QUALIFIER);
        } else {
            return null;
        }
        return qualifiersByPropertyValue.get(qualifier);
    }
}
