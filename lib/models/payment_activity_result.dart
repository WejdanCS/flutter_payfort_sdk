class PaymentActivityResult {
  bool? success;
  String? responseMessage;

  PaymentActivityResult({this.success, this.responseMessage});

  PaymentActivityResult.fromJson(Map<String, dynamic> json) {
    success = json['success'];
    responseMessage = json['response_message'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['success'] = success;
    data['response_message'] = responseMessage;
    return data;
  }
}

