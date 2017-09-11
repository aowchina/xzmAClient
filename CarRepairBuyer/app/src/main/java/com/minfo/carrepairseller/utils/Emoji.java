/**
 * Copyright 2014 www.delight.im <info@delight.im>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** Source: <https://github.com/delight-im/Emoji> */
package com.minfo.carrepairseller.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Lets you replace all emoticons in a text with their corresponding Unicode Emoji
 * <p>
 * Usage: String myEmojiString = Emoji.replaceInText(myString);
 */
public class Emoji {
	
	/** A character class containing special chars often used with emoticons */
	private static final String REGEX_CLASS_SPECIAL_CHARS = "[-_)(;:*<>=/]";
	/** A negative look-behind ensuring that the match is not preceded by one of the special chars above */
	private static final String REGEX_NEGATIVE_LOOKBEHIND = "(?<!"+REGEX_CLASS_SPECIAL_CHARS+")";
	/** A negative look-ahead ensuring that the match is not followed by one of the special chars above */
	private static final String REGEX_NEGATIVE_LOOKAHEAD = "(?!"+REGEX_CLASS_SPECIAL_CHARS+")";
	
	private static class ReplacementsMap extends HashMap<String,Integer> {

		private static final long serialVersionUID = 4948071414363715959L;
		private static ReplacementsMap mInstance;

		private ReplacementsMap() {
			super();
			put(":-)", 0x1F60A);
			put(":)", 0x1F60A);
			put(":-(", 0x1F61E);
			put(":(", 0x1F61E);
			put(":-D", 0x1F603);
			put(":D", 0x1F603);
			put(";-)", 0x1F609);
			put(";)", 0x1F609);
			put(":-P", 0x1F61C);
			put(":P", 0x1F61C);
			put(":-p", 0x1F61C);
			put(":p", 0x1F61C);
			put(":-*", 0x1F618);
			put(":*", 0x1F618);
			put("<3", 0x2764);
			put(":3", 0x2764);
			put(">:[", 0x1F621);
			put(":'|", 0x1F625);
			put(":-[", 0x1F629);
			put(":'(", 0x1F62D);
			put("=O", 0x1F631);
			put("xD", 0x1F601);
			put(":')", 0x1F602);
			put(":-/", 0x1F612);
			put(":/", 0x1F612);
			put(":-|", 0x1F614);
			put(":|", 0x1F614);
			put("*_*", 0x1F60D);
		}
		
		public static ReplacementsMap getInstance() {
			if (mInstance == null) {
				mInstance = new ReplacementsMap();
			}
			return mInstance;
		}

	}
	
	private static String getUnicodeChar(int codepoint) {
		return new String(Character.toChars(codepoint));
	}
	
	/** Constructs a regular expression which ensures that the emoticon is not part of a longer string of special chars (e.g. <:)))> or <http://> which both include basic emoticons) */
	private static String getEmoticonSearchRegex(String emoticon) {
		return REGEX_NEGATIVE_LOOKBEHIND+"("+ Pattern.quote(emoticon)+")"+REGEX_NEGATIVE_LOOKAHEAD;
	}

	/**
	 * Replaces all emoticons in the given text with their corresponding Unicode Emoji
	 * 
	 * @param text the String to search and replace in
	 * @return the new String containing the Unicode Emoji
	 */
	public static String replaceInText(String text) {
		return replaceInText(text, false);
	}

	/**
	 * Converts between emoticons and their corresponding Unicode Emoji in the given text
	 * 
	 * @param text the String to search and replace in
	 * @param reverse whether to replace emoticons with emoji as usual (false) or revert emoji to emoticons (true)
	 * @return the new String containing the the converted entities
	 */
	public static String replaceInText(String text, boolean reverse) {
		final ReplacementsMap replacements = ReplacementsMap.getInstance();
		String emoticon;
		Integer codepoint;

		for (Map.Entry<String, Integer> entry : replacements.entrySet()) {
			if (entry != null) {
				emoticon = entry.getKey();
				codepoint = entry.getValue();
				if (emoticon != null && codepoint != null) {
					String unicodeChar = getUnicodeChar(codepoint.intValue());
					if (reverse) {
						text = text.replace(unicodeChar, emoticon);
					}
					else {
						text = text.replaceAll(getEmoticonSearchRegex(emoticon), unicodeChar);
					}
				}
			}
		}

		return removeNonBmpUnicode(text);
	}

	public static String removeNonBmpUnicode(String str) {
 	   if (str == null) {  
 	       return "";  
 	   }  
 	   str = str.replaceAll("[^\\u0000-\\uFFFF]", "");  
 	   if(TextUtils.isEmpty(str)) {
 		   str = "";
 	   }
 	  return str;  
 }  
}