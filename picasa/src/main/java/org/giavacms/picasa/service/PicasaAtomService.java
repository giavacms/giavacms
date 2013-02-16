/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.giavacms.picasa.service;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.picasa.model.Album;
import org.giavacms.picasa.model.Photo;
import org.giavacms.picasa.model.PicasaConfiguration;
import org.giavacms.picasa.repository.PicasaConfigurationRepository;
import org.giavacms.picasa.service.util.AlbumAccess;
import org.giavacms.picasa.service.util.AuthType;
import org.giavacms.picasa.service.util.PicasaAtomUtils;

import com.google.api.client.http.HttpTransport;
import com.google.api.data.sample.picasa.model.AlbumEntry;
import com.google.api.data.sample.picasa.model.AlbumFeed;
import com.google.api.data.sample.picasa.model.PhotoEntry;
import com.google.api.data.sample.picasa.model.PicasaUrl;
import com.google.api.data.sample.picasa.model.UserFeed;

@Named
@Stateless
public class PicasaAtomService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	PicasaConfigurationRepository picasaConfigurationRepository;

	public List<Album> getAlbumList() throws IOException {
		PicasaConfiguration configuration = picasaConfigurationRepository
				.load();
		return showAlbums(AuthType.CLIENT_LOGIN, configuration.getUsername(),
				configuration.getPassword());
	}

	public List<Photo> getPhotoByAlbum(String url) throws IOException {
		PicasaConfiguration configuration = picasaConfigurationRepository
				.load();
		return showPhotoByAlbumFeedLink(AuthType.CLIENT_LOGIN,
				configuration.getUsername(), configuration.getPassword(), url);
	}

	public List<Album> showAlbums(AuthType authType, String username,
			String password) throws IOException {
		List<Album> albums = new ArrayList<Album>();
		HttpTransport transport = getHttpTransport(authType, username, password);
		// build URL for the default user feed of albums
		PicasaUrl url = PicasaUrl.relativeToRoot("feed/api/user/default");
		// execute GData request for the feed
		UserFeed feed = UserFeed.executeGet(transport, url, 1000);
		System.out.println("User: " + feed.author.name);
		System.out.println("Total number of albums: " + feed.totalResults);
		// show albums
		if (feed.albums != null) {
			for (AlbumEntry albumEntry : feed.albums) {
				Album album = new Album();
				album.setFeedLink(albumEntry.getFeedLink());
				album.setNumPhotos(albumEntry.numPhotos);
				album.setTitle(albumEntry.title);
				album.setSummary(albumEntry.summary);
				albums.add(album);
				// showAlbum(transport, album);
			}
			return albums;
		} else
			return null;
	}

	private List<Photo> showPhotoByAlbumFeedLink(AuthType authType,
			String username, String password, String feedLink)
			throws IOException {
		List<Photo> photos = new ArrayList<Photo>();
		PicasaUrl url = new PicasaUrl(feedLink);
		AlbumFeed feed = AlbumFeed.executeGet(
				getHttpTransport(authType, username, password), url, 3000);
		if (feed.photos != null) {
			int i = 0;
			long start = System.currentTimeMillis();
			for (PhotoEntry photoEntry : feed.photos) {
				Photo photo = new Photo();
				photo.setTitle(photoEntry.title);
				photo.setSummary(photoEntry.summary);
				photo.setImageUrl(photoEntry.mediaGroup.content.url);
				photo.setHeight(photoEntry.mediaGroup.content.height);
				photo.setWidth(photoEntry.mediaGroup.content.width);
				photo.setThumbnailHeight(photoEntry.mediaGroup.thumbnail.height);
				photo.setThumbnailWidth(photoEntry.mediaGroup.thumbnail.width);
				photo.setThumbnailUrl(photoEntry.mediaGroup.thumbnail.url);
				photo.setIndex(start + i);
				photos.add(photo);
				i++;
			}
			return photos;
		} else {
			return null;
		}
	}

	public UserFeed getUserFeed(AuthType authType, String username,
			String password, Integer maxResults) throws IOException {
		PicasaUrl url = PicasaUrl.relativeToRoot("feed/api/user/default");
		UserFeed feed = UserFeed
				.executeGet(getHttpTransport(authType, username, password),
						url, maxResults);
		return feed;
	}

	public HttpTransport getHttpTransport(AuthType authType, String username,
			String password) throws IOException {
		HttpTransport transport = PicasaAtomUtils.setUpTransport(authType,
				username, password);
		return transport;
	}

	public static void showAlbum(HttpTransport transport, AlbumEntry album)
			throws IOException {
		System.out.println();
		System.out.println("-----------------------------------------------");
		System.out.println("Album title: " + album.title);
		System.out.println("Updated: " + album.updated);
		System.out.println("Album ETag: " + album.etag);
		if (album.summary != null) {
			System.out.println("Description: " + album.summary);
		}
		if (album.numPhotos != 0) {
			System.out.println("Total number of photos: " + album.numPhotos);
			PicasaUrl url = new PicasaUrl(album.getFeedLink());
			AlbumFeed feed = AlbumFeed.executeGet(transport, url, null);
			for (PhotoEntry photo : feed.photos) {
				System.out.println();
				System.out.println("Photo title: " + photo.title);
				if (photo.summary != null) {
					System.out.println("Photo description: " + photo.summary);
				}
				System.out.println("Image MIME type: "
						+ photo.mediaGroup.content.type);
				System.out
						.println("Image URL: " + photo.mediaGroup.content.url);
			}
		}
	}

	public AlbumEntry createAlbum(HttpTransport transport, UserFeed feed,
			String albumTitle, AlbumAccess albumAccess, String albumSummary)
			throws IOException {
		AlbumEntry newAlbum = new AlbumEntry();
		newAlbum.access = albumAccess.name().toLowerCase();
		newAlbum.title = albumTitle;
		newAlbum.summary = albumSummary;
		AlbumEntry album = feed.insertAlbum(transport, newAlbum);
		showAlbum(transport, album);
		return album;
	}

	public PhotoEntry createPhoto(HttpTransport transport, AlbumEntry album)
			throws IOException {
		String fileName = "picasaweblogo-en_US.gif";
		String photoUrlString = "http://www.google.com/accounts/lh2/"
				+ fileName;
		URL photoUrl = new URL(photoUrlString);
		PhotoEntry photo = PhotoEntry.executeInsert(transport,
				album.getFeedLink(), photoUrl, fileName);
		System.out.println("Posted photo: " + photo.title);
		return photo;
	}

	public AlbumEntry updatedAlbum(HttpTransport transport, AlbumEntry album)
			throws IOException {
		album = AlbumEntry.executeGet(transport, album.getSelfLink());
		showAlbum(transport, album);
		return album;
	}

	public AlbumEntry updateTitle(HttpTransport transport, AlbumEntry album)
			throws IOException {
		AlbumEntry patched = album.clone();
		patched.title = "My favorite web logos";
		album = patched.executePatchRelativeToOriginal(transport, album);
		showAlbum(transport, album);
		return album;
	}

	public void deleteAlbum(HttpTransport transport, AlbumEntry album)
			throws IOException {
		album.executeDelete(transport);
		System.out.println();
		System.out.println("Album deleted.");
	}
}
