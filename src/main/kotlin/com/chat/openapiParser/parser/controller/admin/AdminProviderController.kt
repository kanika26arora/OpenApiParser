package com.chat.openapiParser.parser.controller.admin

import com.chat.openapiParser.parser.common.ApiErrorResponse
import com.chat.openapiParser.parser.dto.request.ProviderRequest
import com.chat.openapiParser.parser.dto.response.InternalProviderVersionsResponse
import com.chat.openapiParser.parser.dto.response.ProviderResponse
import com.chat.openapiParser.parser.service.ProviderService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/providers")
@Tag(name = "Admin Providers", description = "Admin providers operations")
@OpenAPIDefinition(
    info = Info(title = "Parser Platform Api", description = "Admin Api", version = "1"))
class AdminProviderController(private val providerService: ProviderService) {

  @Operation(summary = "Create a new provider")
  @ApiResponses(value = [ApiResponse(responseCode = "201", description = "Created")])
  @PostMapping
  fun createProvider(@RequestBody provider: ProviderRequest): ResponseEntity<ProviderResponse> {
    return ResponseEntity(providerService.createProvider(provider), HttpStatus.CREATED)
  }

  @Operation(summary = "Get all providers master data")
  @ApiResponses(value = [ApiResponse(responseCode = "200", description = "OK")])
  @GetMapping
  fun getAllProviders(): List<ProviderResponse> {
    return providerService.getAllProviders()
  }

  @Operation(summary = "Get all providers with specification versions master data")
  @ApiResponses(value = [ApiResponse(responseCode = "200", description = "OK")])
  @GetMapping("/with-versions")
  fun getAllProvidersWithSpecificationVersions(): List<InternalProviderVersionsResponse> {
    return providerService.getAllProvidersWithSpecificationVersions()
  }

  @Operation(summary = "Get an existing provider details")
  @ApiResponses(
      value =
          [
              ApiResponse(responseCode = "200", description = "OK"),
              ApiResponse(
                  responseCode = "404",
                  description = "Not found",
                  content =
                      arrayOf(Content(schema = Schema(implementation = ApiErrorResponse::class)))),
          ])
  @GetMapping("/{providerId}")
  fun getProviderDetails(@PathVariable providerId: String): ProviderResponse {
    return providerService.getProviderDetails(providerId)
  }

  @Operation(summary = "Update an existing provider details")
  @ApiResponses(
      value =
          [
              ApiResponse(responseCode = "200", description = "OK"),
              ApiResponse(
                  responseCode = "404",
                  description = "Not found",
                  content =
                      arrayOf(Content(schema = Schema(implementation = ApiErrorResponse::class))))])
  @PutMapping("/{providerId}")
  fun updateProvider(
      @PathVariable providerId: String,
      @RequestBody provider: ProviderRequest
  ): ProviderResponse {
    return providerService.updateProvider(providerId, provider)
  }
}
