package me.steffenjacobs.effectivemusic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import me.steffenjacobs.effectivemusic.domain.TrackMetadata;

/** @author Steffen Jacobs */
@Component
public class OutgoingStatisticsService {

	private static final Logger LOG = LoggerFactory.getLogger(OutgoingStatisticsService.class);

	private static final String URL_STATISTIC_LISTENCOUNT = "http://localhost:8081/files/statistics/listencount";

	public void sendUpdateStatisticInfoIfAvailable(TrackMetadata metadata) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("title", metadata.getTrackDTO().getTitle());
		map.add("artist", metadata.getTrackDTO().getArtist());
		map.add("length", "" + metadata.getTrackDTO().getLength());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForObject(URL_STATISTIC_LISTENCOUNT, request, String.class);
		} catch (RestClientException e) {
			LOG.error(e.getMessage());
		}
	}
}
