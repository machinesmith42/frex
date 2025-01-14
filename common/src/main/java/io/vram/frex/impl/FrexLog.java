/*
 * Copyright © Original Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional copyright and licensing notices may apply for content that was
 * included from other projects. For more information, see ATTRIBUTION.md.
 */

package io.vram.frex.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FrexLog {
	private FrexLog() { }

	public static Logger LOG = LogManager.getLogger("FREX CORE API");

	public static void warn(String string) {
		LOG.warn(string);
	}

	public static void info(String string) {
		LOG.info(string);
	}

	public static void warn(String message, Exception e) {
		LOG.warn(message, e);
	}

	public static void error(Exception e) {
		LOG.error(e);
	}

	public static void error(String error) {
		LOG.error(error);
	}
}
