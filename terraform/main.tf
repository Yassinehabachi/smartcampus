terraform {
  required_version = ">= 1.5"
  required_providers {
    render = {
      source  = "render-oss/render"
      version = "~> 1.0"
    }
  }
}

provider "render" {
  # Reads RENDER_API_KEY and RENDER_OWNER_ID from the environment.
}

variable "repo_url" {
  type    = string
  default = "https://github.com/Yassinehabachi/smartcampus"
}

variable "service_name" {
  type    = string
  default = "smartcampus"
}

variable "plan" {
  type    = string
  default = "free"
}

resource "render_web_service" "api" {
  name   = var.service_name
  plan   = var.plan
  region = "oregon"

  runtime_source = {
    docker = {
      repo_url = var.repo_url
      branch   = "main"
    }
  }

  health_check_path = "/actuator/health"

  env_vars = {
    SPRING_PROFILES_ACTIVE = { value = "demo" }
    JAVA_TOOL_OPTIONS      = { value = "-Xmx320m -XX:+UseSerialGC" }
  }
}

output "service_url" {
  value = render_web_service.api.url
}
