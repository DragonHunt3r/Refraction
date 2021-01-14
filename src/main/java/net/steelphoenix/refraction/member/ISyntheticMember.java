package net.steelphoenix.refraction.member;

/**
 * Represents a possibly synthetic member.
 *
 * @author SteelPhoenix
 */
@FunctionalInterface
public interface ISyntheticMember {

	/**
	 * Get if this member was created by the compiler.
	 *
	 * @return if this member is synthetic.
	 */
	public boolean isSynthetic();
}
