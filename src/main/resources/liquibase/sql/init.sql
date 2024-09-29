CREATE TABLE IF NOT EXISTS `provider`
(
    `id`              varchar(36)  NOT NULL COMMENT 'Unique identifier of the provider',
    `name`            varchar(100) NOT NULL COMMENT 'Name of the provider',
    `logo_url`        varchar(255) NOT NULL COMMENT 'URL of the provider',
    `more_info_url`   varchar(255) COMMENT 'URL to get more info about the provider',
    `created_date`    datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Creation date of the provider entry',
    `modified_date`   datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Last modification date of the provider entry',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT ='Global information about the provider, which is available for integration';


CREATE TABLE IF NOT EXISTS `provider_specification_version`
(
    `id`                   varchar(36)  NOT NULL COMMENT 'Unique identifier of the specification version',
    `provider_id`          varchar(36)  NOT NULL COMMENT 'ID of the provider to which specification version belongs',
    `version_label`        varchar(32)  NOT NULL COMMENT 'Version of the provider specification',
    `open_api_spec_s3_key` varchar(255) NOT NULL COMMENT 'S3 Location of the Open API Spec file',
    `created_date`         datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Creation date of the specification entry',
    `modified_date`        datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Last modification date of the specification entry',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_specification_version_provider` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT ='Global information about the specific provider specification version';


CREATE TABLE IF NOT EXISTS `provider_endpoint`
(
    `id`                                varchar(36)      NOT NULL COMMENT 'Unique identifier of the provider endpoint info',
    `operation_name`                    varchar(100)     NOT NULL COMMENT 'Label for the endpoint',
    `provider_specification_version_id` varchar(36)      NOT NULL COMMENT 'ID of the provider specification version to which endpoint belongs',
    `request_parameters`                JSON             NOT NULL COMMENT 'Input parameters for the endpoint',
    `endpoint_response`                 JSON             NOT NULL COMMENT 'Description of the endpoint responses',
    `endpoint_config`                   JSON             NOT NULL COMMENT 'Detailed configuration of the endpoint',
    `created_date`                      datetime(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Creation date of the endpoint info entry',
    `modified_date`                     datetime(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Last modification date of the endpoint info entry',
    `endpoint_response_template`        json             COMMENT 'template of endpoint response',
    `operation_id`                      SMALLINT UNSIGNED NOT NULL COMMENT 'Operation id of endpoint to uniquely identify'
    PRIMARY KEY (`id`),
    KEY `created_date` (`created_date`),
    KEY `modified_date` (`modified_date`),
    CONSTRAINT `fk_endpoint_info_specification_version` FOREIGN KEY (`provider_specification_version_id`) REFERENCES `provider_specification_version` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT ='Global information about the provider`s endpoints configuration';


CREATE TABLE IF NOT EXISTS `account_provider_config`
(
    `id`                                varchar(32)      NOT NULL COMMENT 'Unique identifier of the configuration',
    `provider_id`                       varchar(32)      NOT NULL COMMENT 'ID of the provider to which configuration belongs',
    `mc2_account_id`                    varchar(32)      NOT NULL COMMENT 'ID of account for which configuration is created',
    `account_provider_config_status_id` tinyint unsigned NOT NULL COMMENT 'Status of the configuration - references configuration_status lookup table',
    `configuration_environment_type_id` tinyint unsigned NOT NULL COMMENT 'Type of the configuration: PRODUCTION, SANDBOX',
    `provider_specification_version_id` varchar(32)      NOT NULL COMMENT 'ID of the specific provider version which is used for the configuration',
    `display_name`                      varchar(45)      NOT NULL COMMENT 'Customer assigned name of the configuration',
    `host_url`                          varchar(255)     NOT NULL COMMENT 'Host of the client`s instance/tenant of the provider',
    `retry_config`                      JSON             NOT NULL COMMENT 'Retry configuration details for the specific configuration',
    `auth_config`                       JSON             NOT NULL COMMENT 'Auth configuration details for the specific configuration',
    `created_date`                      datetime(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Creation date of the configuration entry',
    `modified_date`                     datetime(3)      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Last modification date of the configuration entry',
    PRIMARY KEY (`id`),
    KEY `multi` (`provider_id`, `mc2_account_id`),
    KEY `created_date` (`created_date`),
    KEY `modified_date` (`modified_date`),
    CONSTRAINT `fk_account_provider_config_provider` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`),
    CONSTRAINT `fk_account_provider_config_specification_version` FOREIGN KEY (`provider_specification_version_id`) REFERENCES `provider_specification_version` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT ='Stores configurations created by clients';

CREATE TABLE IF NOT EXISTS `application_token`
(
    `token`            varchar(32) NOT NULL COMMENT 'Unique identifier of the configuration',
    `application_name` varchar(32) NOT NULL COMMENT 'Name of the calling application',
    `enabled`          tinyint(1)  NOT NULL DEFAULT '1' COMMENT 'Flag to check if the application token is enabled- 1-enabled, 0-disabled',
    `created_date`     datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Creation date of the configuration entry',
    `modified_date`    datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Last modification date of the configuration entry',
    PRIMARY KEY (`token`),
    KEY `created_date` (`created_date`),
    KEY `modified_date` (`modified_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT ='Stores application token information to be used by the calling application';


CREATE TABLE IF NOT EXISTS `auth_transaction`
(
    `transaction_id`             varchar(32) NOT NULL COMMENT 'ID of the CommonAuthService auth transaction',
    `account_provider_config_id` varchar(32) NOT NULL COMMENT 'ID of the configuration for which auth was initiated',
    `created_date`               datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Creation date of the auth transaction entry',
    PRIMARY KEY (`transaction_id`),
    CONSTRAINT `fk_auth_transaction_account_provider_config` FOREIGN KEY (`account_provider_config_id`) REFERENCES `account_provider_config` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT ='Temporal storage for relation of auth transaction initiated on CommonAuthService to configuration for which auth was initiated';
