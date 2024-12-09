package br.com.heycheff.api.app.dto.request;

import lombok.Data;

@Data
public class FollowRequest {

    private String userId;
    private String userToFollowId;
}
