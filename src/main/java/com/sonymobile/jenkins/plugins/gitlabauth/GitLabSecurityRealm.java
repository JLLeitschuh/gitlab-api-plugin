package com.sonymobile.jenkins.plugins.gitlabauth;

import net.sf.json.JSONObject;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.springframework.dao.DataAccessException;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.security.AbstractPasswordBasedSecurityRealm;
import hudson.security.GroupDetails;
import hudson.security.SecurityRealm;

/**
 * A security realm to support the use of login in with GitLab credentials to a Jenkins server.
 * 
 * @author Andreas Alanko
 */

public class GitLabSecurityRealm extends AbstractPasswordBasedSecurityRealm {

    @DataBoundConstructor
    public GitLabSecurityRealm() {
        super();
    }

    /**
     * Specifies if the configured Security Realm allows signup.
     * 
     * @return true if signup is allowed
     */
    @Override
    public boolean allowsSignup() {
        return false;
    }

    /**
     * Tries to authenticate a user with the given username and password.
     * 
     * @param username the username of the user
     * @param password the password of the user
     * @return a UserDetails object with user information.
     * @throws AuthenticationException if the authentication fails
     */
    @Override
    protected UserDetails authenticate(String username, String password) throws AuthenticationException {
        UserDetails userDetails = null;

        if (isValidUser(username, password)) {
            userDetails = new User(username, password, true, true, true, true, new GrantedAuthority[] { SecurityRealm.AUTHENTICATED_AUTHORITY });
        } else {
            throw new BadCredentialsException("Not a valid username or password");
        }

        return userDetails;
    }

    /**
     * Checks if a user with matching username and password exists.
     * 
     * @param username the username of the user
     * @param password  the password of the user
     * @return true if there exist a user with username and a matching password
     */
    private boolean isValidUser(String username, String password) {
        return username.equals("nisse") && password.equals("p2");
    }
    
    /**
     * Gets user information about the user with the given username.
     * 
     * @param username the user of the user
     * @return a UserDetails object with information about the user
     * @throws UsernameNotFoundException if user with username does not exist
     * @throws DataAccessException will never be thrown
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        //TODO
        return null;
    }

    /**
     * This feature is not supported.
     * Will throw UsernameNotFoundException at all times.
     * 
     * @params username the username of the user
     * @throws UsernameNotFoundException will be thrown at all times.
     * @throws DataAccessException will never be thrown.
     */
    @Override
    public GroupDetails loadGroupByGroupname(String groupname) throws UsernameNotFoundException, DataAccessException {
        throw new UsernameNotFoundException("Feature not supported");
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {

        /**
         * Returns a new GitLabSecurityRealm object
         * 
         * @param req the http request
         * @param formData form data f
         * @return a GitLabSecurityRealm object
         */
        @Override
        public SecurityRealm newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return new GitLabSecurityRealm();
        }

        /**
         * Gives the name to be displayed by the Jenkins view in the security configuration page.
         * 
         * @return the display name
         */
        public String getDisplayName() {
            return "GitLab Authentication";
        }
    }
}