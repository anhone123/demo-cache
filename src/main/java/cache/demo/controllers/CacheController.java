package cache.demo.controllers;

import static cache.demo.constains.CacheNames.ALL_CACHE_STRING_NAMES_SAMPLE;

import cache.demo.controllers.endpoint.DemoCacheEndpoint;
import cache.demo.exceptions.ApplicationException;
import cache.demo.exceptions.CacheNotFoundException;
import cache.demo.service.merchant.CacheService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DemoCacheEndpoint.CACHE_MANAGEMENT)
@RequiredArgsConstructor
@Slf4j
public class CacheController {

  private final CacheService cacheService;

  @GetMapping(DemoCacheEndpoint.CACHE_GET)
  @ApiOperation(value = "This method is used to GET value of a cache by key." )
  @ApiResponses(@ApiResponse(description = ALL_CACHE_STRING_NAMES_SAMPLE))
  public Object getCacheValueBy(@RequestParam String cacheName,
      @RequestParam(required = false) String stringKey)
      throws ApplicationException {
    return cacheService.getCacheValueBy(cacheName, stringKey);
  }

  @GetMapping(DemoCacheEndpoint.CACHE_UPDATE)
  @ApiOperation(value = "This method is used to UPDATE value of cache by key")
  @ApiResponses(@ApiResponse(description = ALL_CACHE_STRING_NAMES_SAMPLE))
  public void setCacheValue(@RequestParam String cacheName,
      @RequestParam String key,
      @RequestParam String stringValue)
      throws CacheNotFoundException {
    cacheService.setCacheValue(cacheName, key, stringValue);
  }

  @DeleteMapping(DemoCacheEndpoint.CACHE_CLEAR_BY_KEY)
  @ApiOperation(value = "This method is used to CLEAR a cache value")
  @ApiResponses(@ApiResponse(description = ALL_CACHE_STRING_NAMES_SAMPLE))
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void clearCacheValueBy(@RequestParam String cacheName,
      @RequestParam String stringKey)
      throws CacheNotFoundException {
    cacheService.clearCacheValueBy(cacheName, stringKey);
  }

  @DeleteMapping(DemoCacheEndpoint.CACHE_CLEAR_ALL)
  @ApiOperation(value = "This method is used to CLEAR ALL value of cache by cacheName")
  @ApiResponses(@ApiResponse(description = ALL_CACHE_STRING_NAMES_SAMPLE))
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void clearAllCacheValueOf(@RequestParam String cacheName)
      throws CacheNotFoundException {
    cacheService.clearAllCacheValueOf(cacheName);
  }
}

