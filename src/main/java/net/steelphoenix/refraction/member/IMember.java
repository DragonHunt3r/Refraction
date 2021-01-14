package net.steelphoenix.refraction.member;

/**
 * Represents a member.
 * Note that some methods may not be relevant for certain implementing classes.
 *
 * @author SteelPhoenix
 */
public interface IMember extends IAccessibleMember, IAnnotatableMember, ISyntheticMember {

	/**
	 * Get the class declaring this member.
	 *
	 * @return the declaring class.
	 */
	public Class<?> getDeclarer();

	/**
	 * Get the name of this member.
	 *
	 * @return the name.
	 */
	public String getName();
}
