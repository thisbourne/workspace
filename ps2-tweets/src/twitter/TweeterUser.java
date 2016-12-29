package twitter;

//import java.time.Instant;

/**
 * This immutable datatype represents a tweet from Twitter.
 * 
 * DO NOT CHANGE THIS CLASS.
 */
public class TweeterUser {

    private final String name;
    private final int followers;
    /* Rep invariant: 
     *    name.length > 0
     *    all characters in name are drawn from {A..Z, a..z, 0..9, _, -}
     */
    
    /**
     * Make a TweeterUser.
     * 
     * @param name
     *            Twitter username.  
     *            Required to be a Twitter username as defined by getName() below.
     * @param followers
     *            integer number of users following this user.
     */
    public TweeterUser(final String name, final int followers) {
        this.name = name;
        this.followers = followers;
    }


    /**
     * @return Twitter username.
     *         A Twitter username is a nonempty sequence of letters (A-Z or
     *         a-z), digits, underscore ("_"), or hyphen ("-").
     *         Twitter usernames are case-insensitive, so "jbieber" and "JBieBer"
     *         are equivalent.
     */
    public String getName() {
        return name;
    }

    /**
     * @return # of followers of this user
     */
    public int getFollowers() {
        return followers;
    }



    /*
     * @see Object.toString()
     */
    @Override public String toString() {
        return "(User: " + this.getName()
                + " ; Followers: " + this.getFollowers()
                + ") ";
    }

    /*
     * @see Object.equals()
     */
    @Override public boolean equals(Object thatObject) {
        if (!(thatObject instanceof TweeterUser)) {
            return false;
        }

        TweeterUser that = (TweeterUser) thatObject;
        return this.name == that.name;
    }

    /*
     * @see Object.hashCode()
     */
    @Override public int hashCode() {
        final int bitsInInt = 32;
        final int lower32bits = (int) followers;
        final int upper32bits = (int) (followers >> bitsInInt);
        return lower32bits ^ upper32bits;
    }


}
