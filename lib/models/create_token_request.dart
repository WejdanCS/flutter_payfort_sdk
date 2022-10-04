class CreateTokenRequest {
  String? serviceCommand;
  String? accessCode;
  String? merchantIdentifier;
  String? language;
  String? deviceId;

  CreateTokenRequest(
      {this.serviceCommand,
        this.accessCode,
        this.merchantIdentifier,
        this.language,
        this.deviceId});

  CreateTokenRequest.fromJson(Map<String, dynamic> json) {
    serviceCommand = json['service_command'];
    accessCode = json['access_code'];
    merchantIdentifier = json['merchant_identifier'];
    language = json['language'];
    deviceId = json['device_id'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['service_command'] = serviceCommand;
    data['access_code'] = accessCode;
    data['merchant_identifier'] = merchantIdentifier;
    data['language'] = language;
    data['device_id'] = deviceId;
    return data;
  }
}