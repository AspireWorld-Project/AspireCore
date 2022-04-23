package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

public abstract class ChatComponentStyle implements IChatComponent {
	@SuppressWarnings("rawtypes")
	protected List siblings = Lists.newArrayList();
	private ChatStyle style;
	@SuppressWarnings("unchecked")
	@Override
	public IChatComponent appendSibling(IChatComponent p_150257_1_) {
		p_150257_1_.getChatStyle().setParentStyle(getChatStyle());
		siblings.add(p_150257_1_);
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getSiblings() {
		return siblings;
	}

	@Override
	public IChatComponent appendText(String p_150258_1_) {
		return appendSibling(new ChatComponentText(p_150258_1_));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IChatComponent setChatStyle(ChatStyle p_150255_1_) {
		style = p_150255_1_;
		Iterator iterator = siblings.iterator();

		while (iterator.hasNext()) {
			IChatComponent ichatcomponent = (IChatComponent) iterator.next();
			ichatcomponent.getChatStyle().setParentStyle(getChatStyle());
		}

		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ChatStyle getChatStyle() {
		if (style == null) {
			style = new ChatStyle();
			Iterator iterator = siblings.iterator();

			while (iterator.hasNext()) {
				IChatComponent ichatcomponent = (IChatComponent) iterator.next();
				ichatcomponent.getChatStyle().setParentStyle(style);
			}
		}

		return style;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Iterator iterator() {
		return Iterators.concat(Iterators.forArray(this),
				createDeepCopyIterator(siblings));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public final String getUnformattedText() {
		StringBuilder stringbuilder = new StringBuilder();
		Iterator iterator = iterator();

		while (iterator.hasNext()) {
			IChatComponent ichatcomponent = (IChatComponent) iterator.next();
			stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
		}

		return stringbuilder.toString();
	}

	// @SideOnly(Side.CLIENT)
	@SuppressWarnings("rawtypes")
	@Override
	public final String getFormattedText() {
		StringBuilder stringbuilder = new StringBuilder();
		Iterator iterator = iterator();

		while (iterator.hasNext()) {
			IChatComponent ichatcomponent = (IChatComponent) iterator.next();
			stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
			stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
			stringbuilder.append(EnumChatFormatting.RESET);
		}

		return stringbuilder.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Iterator createDeepCopyIterator(Iterable p_150262_0_) {
		Iterator iterator = Iterators.concat(Iterators.transform(p_150262_0_.iterator(), new Function() {
			public Iterator apply(IChatComponent p_apply_1_) {
				return p_apply_1_.iterator();
			}

			@Override
			public Object apply(Object p_apply_1_) {
				return this.apply((IChatComponent) p_apply_1_);
			}
		}));
		iterator = Iterators.transform(iterator, new Function() {
			public IChatComponent apply(IChatComponent p_apply_1_) {
				IChatComponent ichatcomponent1 = p_apply_1_.createCopy();
				ichatcomponent1.setChatStyle(ichatcomponent1.getChatStyle().createDeepCopy());
				return ichatcomponent1;
			}

			@Override
			public Object apply(Object p_apply_1_) {
				return this.apply((IChatComponent) p_apply_1_);
			}
		});
		return iterator;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ChatComponentStyle))
			return false;
		else {
			ChatComponentStyle chatcomponentstyle = (ChatComponentStyle) p_equals_1_;
			return siblings.equals(chatcomponentstyle.siblings)
					&& getChatStyle().equals(chatcomponentstyle.getChatStyle());
		}
	}

	@Override
	public int hashCode() {
		return 31 * style.hashCode() + siblings.hashCode();
	}

	@Override
	public String toString() {
		return "BaseComponent{style=" + style + ", siblings=" + siblings + '}';
	}
}
