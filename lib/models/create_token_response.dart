class CreateTokenResponse {
  bool?success;
  Result? result;
  String? merchantReference;
  String? signature;

  CreateTokenResponse({this.success,this.result, this.merchantReference, this.signature});

  CreateTokenResponse.fromJson(Map<String, dynamic> json) {
    success= json['success'];
    result =
    json['result'] != null ?  Result.fromJson(json['result']) : null;
    merchantReference = json['merchant_reference'];
    signature = json['signature'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data["success"]=success;
    if (result != null) {
      data['result'] = result!.toJson();
    }
    data['merchant_reference'] = merchantReference;
    data['signature'] =signature;
    return data;
  }
}

class Result {
  String? responseCode;
  String? deviceId;
  String? responseMessage;
  String? serviceCommand;
  String? sdkToken;
  String? signature;
  String? merchantIdentifier;
  String? accessCode;
  String? language;
  String? status;

  Result(
      {this.responseCode,
        this.deviceId,
        this.responseMessage,
        this.serviceCommand,
        this.sdkToken,
        this.signature,
        this.merchantIdentifier,
        this.accessCode,
        this.language,
        this.status});

  Result.fromJson(Map<String, dynamic> json) {
    responseCode = json['response_code'];
    deviceId = json['device_id'];
    responseMessage = json['response_message'];
    serviceCommand = json['service_command'];
    sdkToken = json['sdk_token'];
    signature = json['signature'];
    merchantIdentifier = json['merchant_identifier'];
    accessCode = json['access_code'];
    language = json['language'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['response_code'] =responseCode;
    data['device_id'] = deviceId;
    data['response_message'] = responseMessage;
    data['service_command'] = serviceCommand;
    data['sdk_token'] = sdkToken;
    data['signature'] = signature;
    data['merchant_identifier'] = merchantIdentifier;
    data['access_code'] = accessCode;
    data['language'] = language;
    data['status'] = status;
    return data;
  }
}

