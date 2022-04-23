package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatComponentTranslation extends ChatComponentStyle {
	private final String key;
	private final Object[] formatArgs;
	private final Object syncLock = new Object();
	private long lastTranslationUpdateTimeInMilliseconds = -1L;
	List children = Lists.newArrayList();
	public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
	private static final String __OBFID = "CL_00001270";

	public ChatComponentTranslation(String p_i45160_1_, Object... p_i45160_2_) {
		key = p_i45160_1_;
		formatArgs = p_i45160_2_;
		Object[] aobject = p_i45160_2_;
		int i = p_i45160_2_.length;

		for (int j = 0; j < i; ++j) {
			Object object1 = aobject[j];

			if (object1 instanceof IChatComponent) {
				((IChatComponent) object1).getChatStyle().setParentStyle(getChatStyle());
			}
		}
	}

	synchronized void ensureInitialized() {
		synchronized (syncLock) {
			long i = StatCollector.getLastTranslationUpdateTimeInMilliseconds();

			if (i == lastTranslationUpdateTimeInMilliseconds)
				return;

			lastTranslationUpdateTimeInMilliseconds = i;
			children.clear();
		}

		try {
			initializeFromFormat(StatCollector.translateToLocal(key));
		} catch (ChatComponentTranslationFormatException chatcomponenttranslationformatexception1) {
			children.clear();

			try {
				initializeFromFormat(StatCollector.translateToFallback(key));
			} catch (ChatComponentTranslationFormatException chatcomponenttranslationformatexception) {
				throw chatcomponenttranslationformatexception1;
			}
		}
	}

	protected void initializeFromFormat(String p_150269_1_) {
		Matcher matcher = stringVariablePattern.matcher(p_150269_1_);
		int i = 0;
		int j = 0;

		try {
			int l;

			for (; matcher.find(j); j = l) {
				int k = matcher.start();
				l = matcher.end();

				if (k > j) {
					ChatComponentText chatcomponenttext = new ChatComponentText(
							String.format(p_150269_1_.substring(j, k), new Object[0]));
					chatcomponenttext.getChatStyle().setParentStyle(getChatStyle());
					children.add(chatcomponenttext);
				}

				String s3 = matcher.group(2);
				String s1 = p_150269_1_.substring(k, l);

				if ("%".equals(s3) && "%%".equals(s1)) {
					ChatComponentText chatcomponenttext2 = new ChatComponentText("%");
					chatcomponenttext2.getChatStyle().setParentStyle(getChatStyle());
					children.add(chatcomponenttext2);
				} else {
					if (!"s".equals(s3))
						throw new ChatComponentTranslationFormatException(this, "Unsupported format: \'" + s1 + "\'");

					String s2 = matcher.group(1);
					int i1 = s2 != null ? Integer.parseInt(s2) - 1 : i++;
					children.add(getFormatArgumentAsComponent(i1));
				}
			}

			if (j < p_150269_1_.length()) {
				ChatComponentText chatcomponenttext1 = new ChatComponentText(
						String.format(p_150269_1_.substring(j), new Object[0]));
				chatcomponenttext1.getChatStyle().setParentStyle(getChatStyle());
				children.add(chatcomponenttext1);
			}
		} catch (IllegalFormatException illegalformatexception) {
			throw new ChatComponentTranslationFormatException(this, illegalformatexception);
		}
	}

	private IChatComponent getFormatArgumentAsComponent(int p_150272_1_) {
		if (p_150272_1_ >= formatArgs.length)
			throw new ChatComponentTranslationFormatException(this, p_150272_1_);
		else {
			Object object = formatArgs[p_150272_1_];
			Object object1;

			if (object instanceof IChatComponent) {
				object1 = object;
			} else {
				object1 = new ChatComponentText(object == null ? "null" : object.toString());
				((IChatComponent) object1).getChatStyle().setParentStyle(getChatStyle());
			}

			return (IChatComponent) object1;
		}
	}

	@Override
	public IChatComponent setChatStyle(ChatStyle p_150255_1_) {
		super.setChatStyle(p_150255_1_);
		Object[] aobject = formatArgs;
		int i = aobject.length;

		for (int j = 0; j < i; ++j) {
			Object object = aobject[j];

			if (object instanceof IChatComponent) {
				((IChatComponent) object).getChatStyle().setParentStyle(getChatStyle());
			}
		}

		if (lastTranslationUpdateTimeInMilliseconds > -1L) {
			Iterator iterator = children.iterator();

			while (iterator.hasNext()) {
				IChatComponent ichatcomponent = (IChatComponent) iterator.next();
				ichatcomponent.getChatStyle().setParentStyle(p_150255_1_);
			}
		}

		return this;
	}

	@Override
	public Iterator iterator() {
		ensureInitialized();
		return Iterators.concat(createDeepCopyIterator(children), createDeepCopyIterator(siblings));
	}

	@Override
	public String getUnformattedTextForChat() {
		ensureInitialized();
		StringBuilder stringbuilder = new StringBuilder();
		Iterator iterator = children.iterator();

		while (iterator.hasNext()) {
			IChatComponent ichatcomponent = (IChatComponent) iterator.next();
			stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
		}

		return stringbuilder.toString();
	}

	@Override
	public ChatComponentTranslation createCopy() {
		Object[] aobject = new Object[formatArgs.length];

		for (int i = 0; i < formatArgs.length; ++i) {
			if (formatArgs[i] instanceof IChatComponent) {
				aobject[i] = ((IChatComponent) formatArgs[i]).createCopy();
			} else {
				aobject[i] = formatArgs[i];
			}
		}

		ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(key, aobject);
		chatcomponenttranslation.setChatStyle(getChatStyle().createShallowCopy());
		Iterator iterator = getSiblings().iterator();

		while (iterator.hasNext()) {
			IChatComponent ichatcomponent = (IChatComponent) iterator.next();
			chatcomponenttranslation.appendSibling(ichatcomponent.createCopy());
		}

		return chatcomponenttranslation;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ChatComponentTranslation))
			return false;
		else {
			ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation) p_equals_1_;
			return Arrays.equals(formatArgs, chatcomponenttranslation.formatArgs)
					&& key.equals(chatcomponenttranslation.key) && super.equals(p_equals_1_);
		}
	}

	@Override
	public int hashCode() {
		int i = super.hashCode();
		i = 31 * i + key.hashCode();
		i = 31 * i + Arrays.hashCode(formatArgs);
		return i;
	}

	@Override
	public String toString() {
		return "TranslatableComponent{key=\'" + key + '\'' + ", args=" + Arrays.toString(formatArgs) + ", siblings="
				+ siblings + ", style=" + getChatStyle() + '}';
	}

	public String getKey() {
		return key;
	}

	public Object[] getFormatArgs() {
		return formatArgs;
	}
}