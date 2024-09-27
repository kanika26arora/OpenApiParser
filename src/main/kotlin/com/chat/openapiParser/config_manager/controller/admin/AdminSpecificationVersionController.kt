package com.chat.openapiParser.config_manager.controller.admin

import com.chat.openapiParser.config_manager.common.ApiErrorResponse
import com.chat.openapiParser.config_manager.dto.request.ProviderSpecificationVersionRequest
import com.chat.openapiParser.config_manager.dto.request.ProviderSpecificationVersionUpdateRequest
import com.chat.openapiParser.config_manager.dto.response.ProviderSpecificationVersionResponse
import com.chat.openapiParser.config_manager.dto.response.SpecificationResponse
import com.chat.openapiParser.config_manager.dto.response.UploadSpecificationResponse
import com.chat.openapiParser.config_manager.service.SpecificationVersionService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/admin/specifications")
@Tag(name = "Admin Specifications", description = "Admin specifications operations")
@OpenAPIDefinition(
    info = Info(title = "Chathub Platform Api", description = "Admin Api", version = "1"))
class AdminSpecificationVersionController(
    private val specificationVersionService: SpecificationVersionService
) {

  @Operation(summary = "Upload specification")
  @ApiResponses(
      value =
          [
              ApiResponse(responseCode = "200", description = "OK"),
              ApiResponse(
                  responseCode = "400",
                  description = "Bad request",
                  content =
                      arrayOf(Content(schema = Schema(implementation = ApiErrorResponse::class)))),
          ])
  @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun uploadSpecification(
      @RequestParam providerId: String,
      @RequestParam versionLabel: String,
      @Parameter(name = "file", schema = Schema(type = "string", format = "binary"))
      @RequestPart
      file: MultipartFile
  ): UploadSpecificationResponse {
    return UploadSpecificationResponse(
        specificationVersionService.uploadSpecification(providerId, versionLabel, file))
  }

  @Operation(summary = "Create new provider specification")
  @ApiResponses(
      value =
          [
              ApiResponse(responseCode = "200", description = "OK"),
              ApiResponse(
                  responseCode = "400",
                  description = "Bad request",
                  content =
                      arrayOf(Content(schema = Schema(implementation = ApiErrorResponse::class)))),
          ])
  @PostMapping
  fun createSpecificationForProvider(
      @RequestParam providerId: String,
      @RequestBody specificationRequest: ProviderSpecificationVersionRequest
  ): ProviderSpecificationVersionResponse {
    return specificationVersionService.createSpecificationForProvider(
        providerId, specificationRequest)
  }

  @Operation(summary = "Get all specifications for a provider")
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
  @GetMapping
  fun getSpecificationsForProvider(@RequestParam providerId: String): List<SpecificationResponse> {
    return specificationVersionService.getSpecificationsForProvider(providerId)
  }

  @Operation(summary = "Update provider specification")
  @ApiResponses(
      value =
          [
              ApiResponse(responseCode = "200", description = "OK"),
              ApiResponse(
                  responseCode = "404",
                  description = "Not found",
                  content =
                      arrayOf(Content(schema = Schema(implementation = ApiErrorResponse::class)))),
              ApiResponse(
                  responseCode = "400",
                  description = "Bad request",
                  content =
                      arrayOf(Content(schema = Schema(implementation = ApiErrorResponse::class))))])
  @PutMapping("/{specificationId}")
  fun updateSpecificationForProvider(
      @PathVariable specificationId: String,
      @RequestBody specificationRequest: ProviderSpecificationVersionUpdateRequest
  ): ProviderSpecificationVersionResponse {
    return specificationVersionService.updateSpecificationForProvider(
        specificationId, specificationRequest)
  }
}
