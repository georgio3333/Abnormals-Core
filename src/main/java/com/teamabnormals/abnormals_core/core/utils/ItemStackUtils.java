package com.teamabnormals.abnormals_core.core.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author SmellyModder(Luke Tonon)
 */
public class ItemStackUtils {

	/**
	 * Searches for a specific item in a {@link NonNullList} of {@link ItemStack} and returns its index
	 * @param item - The item to search for
	 * @param items - The list of ItemStacks
	 * @return - The index of the specified item in the list, if no item was found returns -1
	 */
	public static int findIndexOfItem(Item item, NonNullList<ItemStack> items) {
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getItem() == item) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Converts an Integer to a String of Roman Numerals; useful for levels
	 * @param number - The Integer to convert
	 * @return - The String of the Integer converted to Roman Numerals
	 */
	public static String intToRomanNumerals(int number) {
		String m[] = {"", "M", "MM", "MMM"}; 
		String c[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"}; 
		String x[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"}; 
		String i[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
		
		String thousands = m[number / 1000]; 
		String hundereds = c[(number % 1000) / 100]; 
		String tens = x[(number % 100) / 10]; 
		String ones = i[number % 10];
		
		return thousands + hundereds + tens + ones;
	}
	
}