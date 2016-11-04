package com.harris.carolyn.lab302.beans;

import org.springframework.social.twitter.api.DirectMessageOperations;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.GeoOperations;
import org.springframework.social.twitter.api.ListOperations;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.UserOperations;

public interface Twitter {

	boolean isAuthorizedForUser();

	   DirectMessageOperations directMessageOperations();

	   FriendOperations friendOperations();

	   GeoOperations geoOperations();

	   ListOperations listOperations();

	   SearchOperations searchOperations();

	   TimelineOperations timelineOperations();

	   UserOperations userOperations();

	
}
