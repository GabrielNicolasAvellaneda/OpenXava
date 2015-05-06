/**
 * 
 */
package org.openxava.tab;

/**
 * @author Federico Alcantara
 *
 */
public class NoPreferencesTab extends Tab {

	/**
	 * Prevented here to save preferences.
	 * @see org.openxava.tab.Tab#saveUserPreferences()
	 */
	@Override
	protected void saveUserPreferences() {
	}
}
