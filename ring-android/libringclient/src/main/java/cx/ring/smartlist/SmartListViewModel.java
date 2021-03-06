/*
 *  Copyright (C) 2004-2019 Savoir-faire Linux Inc.
 *
 *  Author: Hadrien De Sousa <hadrien.desousa@savoirfairelinux.com>
 *  Author: Adrien Béraud <adrien.beraud@savoirfairelinux.com>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package cx.ring.smartlist;

import java.util.Collections;
import java.util.List;

import cx.ring.model.CallContact;
import cx.ring.model.Conversation;
import cx.ring.model.Interaction;
import cx.ring.model.Uri;
import cx.ring.services.AccountService;
import io.reactivex.Observable;
import io.reactivex.Single;

public class SmartListViewModel
{
    public static final Observable<SmartListViewModel> TITLE_CONVERSATIONS = Observable.just(new SmartListViewModel(Title.Conversations));
    public static final Observable<SmartListViewModel> TITLE_PUBLIC_DIR = Observable.just(new SmartListViewModel(Title.PublicDirectory));
    public static final Single<List<Observable<SmartListViewModel>>> EMPTY_LIST = Single.just(Collections.emptyList());
    public static final Observable<List<SmartListViewModel>> EMPTY_RESULTS = Observable.just(Collections.emptyList());

    private final String accountId;
    private final Uri uri;
    private final List<CallContact> contact;
    private final String uuid;
    private final String contactName;
    private final boolean hasUnreadTextMessage;
    private boolean hasOngoingCall;

    private final boolean showPresence;
    //private boolean isOnline = false;
    private boolean isChecked = false;
    private final Interaction lastEvent;

    public enum Title {
        None,
        Conversations,
        PublicDirectory
    }
    private final Title title;

    public String picture_b64 = null;

    public SmartListViewModel(String accountId, CallContact contact, Interaction lastEvent) {
        this.accountId = accountId;
        this.contact = Collections.singletonList(contact);
        this.uri = contact.getPrimaryUri();
        uuid = uri.getRawUriString();
        this.contactName = contact.getDisplayName();
        hasUnreadTextMessage = (lastEvent != null) && !lastEvent.isRead();
        this.hasOngoingCall = false;
        this.lastEvent = lastEvent;
        showPresence = true;
        //isOnline = contact.isOnline();
        title = Title.None;
    }
    public SmartListViewModel(String accountId, CallContact contact, String id, Interaction lastEvent) {
        this.accountId = accountId;
        this.contact = Collections.singletonList(contact);
        uri = contact.getPrimaryUri();
        this.uuid = id;
        this.contactName = contact.getDisplayName();
        hasUnreadTextMessage = (lastEvent != null) && !lastEvent.isRead();
        this.hasOngoingCall = false;
        this.lastEvent = lastEvent;
        showPresence = true;
        //isOnline = contact.isOnline();
        title = Title.None;
    }
    public SmartListViewModel(Conversation conversation, List<CallContact> contacts, boolean presence) {
        this.accountId = conversation.getAccountId();
        this.contact = contacts;
        uri = conversation.getUri();
        this.uuid = uri.getRawUriString();
        this.contactName = conversation.getTitle();
        Interaction lastEvent = conversation.getLastEvent();
        hasUnreadTextMessage = (lastEvent != null) && !lastEvent.isRead();
        this.hasOngoingCall = false;
        this.lastEvent = lastEvent;
        //isOnline = contact.isOnline();
        showPresence = presence;
        title = Title.None;
    }
    public SmartListViewModel(Conversation conversation, boolean presence) {
        this(conversation, Collections.singletonList(conversation.getContact()), presence);
    }

    private SmartListViewModel(Title title) {
        contactName = null;
        this.accountId = null;
        this.contact = null;
        this.uuid = null;
        uri = null;
        hasUnreadTextMessage = false;
        lastEvent = null;
        picture_b64 = null;
        showPresence = false;
        this.title = title;
    }

    public Uri getUri() {
        return uri;
    }

    public CallContact getContact() {
        return contact == null ? null : contact.get(0);
    }

    public String getContactName() {
        return contactName;
    }

    public long getLastInteractionTime() {
        return (lastEvent == null) ? 0 : lastEvent.getTimestamp();
    }

    public boolean hasUnreadTextMessage() {
        return hasUnreadTextMessage;
    }

    public boolean hasOngoingCall() {
        return hasOngoingCall;
    }

    public String getUuid() {
        return uuid;
    }

    /*public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        if (showPresence)
            isOnline = online;
    }*/

    public boolean showPresence() {
        return showPresence;
    }

    public boolean isChecked() { return isChecked; }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setHasOngoingCall(boolean hasOngoingCall) {
        this.hasOngoingCall = hasOngoingCall;
    }

    public Interaction getLastEvent() {
        return lastEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SmartListViewModel))
            return false;
        SmartListViewModel other = (SmartListViewModel) o;
        return other.getHeaderTitle() == getHeaderTitle()
                && (getHeaderTitle() != Title.None
                || (contact == other.contact
                && contactName.equals(other.contactName)
                //&& isOnline == other.isOnline
                && lastEvent == other.lastEvent
                && hasOngoingCall == other.hasOngoingCall
                && hasUnreadTextMessage == other.hasUnreadTextMessage));
    }

    public String getAccountId() {
        return accountId;
    }

    public Title getHeaderTitle() {
        return title;
    }
}
