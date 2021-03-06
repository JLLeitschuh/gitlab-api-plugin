/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Andreas Alanko, Emil Nilsson, Sony Mobile Communications AB.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sonymobile.gitlab.model;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.sonymobile.gitlab.helpers.DateHelpers.utcDate;
import static com.sonymobile.gitlab.helpers.JsonFileLoader.jsonFile;
import static java.util.Calendar.NOVEMBER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;

/**
 * Tests getting attributes from a {@link com.sonymobile.gitlab.model.GitLabSessionInfo} object.
 *
 * @author Emil Nilsson
 */
public class FullUserTest {
    /** A rule for catching expected exceptions. */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /** A normal user. */
    private GitLabUserInfo normalUser;

    /** A blocked user. */
    private GitLabUserInfo blockedUser;

    /** An admin user. */
    private GitLabUserInfo adminUser;

    /**
     * Loads the user objects from a JSON file.
     */
    @Before
    public void setUp() throws Exception {
        normalUser = jsonFile("api/v3/users/1")
                .withType(FullGitLabUserInfo.class)
                .loadAsObject();

        blockedUser = jsonFile("api/v3/users/1")
                .withType(FullGitLabUserInfo.class)
                .withVariant("blocked")
                .loadAsObject();

        adminUser = jsonFile("api/v3/users/1")
                .withType(FullGitLabUserInfo.class)
                .withVariant("admin")
                .loadAsObject();
    }

    @Test
    public void getId() {
        assertThat(normalUser.getId(), is(1));
    }

    @Test
    public void getUsername() {
        assertThat(normalUser.getUsername(), is("username"));
    }

    @Test
    public void getEmail() {
        assertThat(normalUser.getEmail(), is("user@example.com"));
    }

    @Test
    public void getName() {
        assertThat(normalUser.getName(), is("User Name"));
    }

    @Test
    public void getCreatedAtDate() {
        assertThat(normalUser.getCreatedAtDate(), is(utcDate(2010, NOVEMBER, 12, 13, 14, 15)));
    }

    @Test
    public void isActive() {
        assertThat(normalUser.isActive(), is(true));
        assertThat(blockedUser.isActive(), is(false));
    }

    @Test
    public void isAdmin() {
        assertThat(normalUser.isAdmin(), is(false));
        assertThat(adminUser.isAdmin(), is(true));
    }

    /**
     * Attempts to create user info with missing keys.
     */
    @Test
    public void createUserWithMissingKeys() throws Exception {
        // constructor should throw an exception
        thrown.expect(IllegalArgumentException.class);
        // use empty JSON object
        new FullGitLabUserInfo(new JSONObject());
    }

    /**
     * Converts the user info to a String, which should be the username
     */
    @Test
    public void convertToString() {
        assertThat(normalUser, hasToString("username"));
    }
}
