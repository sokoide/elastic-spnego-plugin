package com.sokoide.elastic;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class SoKoideAuthResponse extends ActionResponse implements ToXContentObject {

  private Throwable throwable;
  private String body;

  public SoKoideAuthResponse(String body) {
    this.body = body;
  }

  public SoKoideAuthResponse(Throwable t) {
    this.throwable = t;
  }

  @Override
  public void writeTo(StreamOutput out) throws IOException {
    super.writeTo(out);
  }

  @Override
  public void readFrom(StreamInput in) throws IOException {
    super.readFrom(in);
  }

  @Override
  public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
    builder.startObject();
    if (throwable == null) {
      builder.field("status", "ok").field("message", body);
    }
    else {
      builder.field("status", "ko").field("message", throwable.getMessage());
    }
    builder.endObject();
    return builder;
  }
}