package com.depromeet.sloth.data.network.member

import com.depromeet.sloth.BuildConfig
import com.depromeet.sloth.data.network.RetrofitServiceGenerator

class MemberRepository {
    suspend fun fetchMemberInfo(accessToken: String): MemberState<MemberInfoResponse> {
        RetrofitServiceGenerator.setBuilderOptions(
            targetUrl = BuildConfig.SLOTH_BASE_URL,
            authToken = accessToken
        )
            .create(MemberService::class.java)
            .fetchMemberInfo()?.run {
                return when(this.code()) {
                    200 -> MemberState.Success(this.body() ?: MemberInfoResponse())
                    401 -> MemberState.Unauthorized
                    403 -> MemberState.Forbidden
                    404 -> MemberState.NotFound
                    else -> MemberState.Error(Exception("Uncaught Exception"))
                }
            } ?: return MemberState.Error(Exception("Retrofit Exception"))
    }

    suspend fun updateMemberInfo(
        accessToken: String,
        memberUpdateInfoRequest: MemberUpdateInfoRequest
    ): MemberState<MemberUpdateInfoResponse> {
        RetrofitServiceGenerator.setBuilderOptions(
            targetUrl = BuildConfig.SLOTH_BASE_URL,
            authToken = accessToken
        )
            .create(MemberService::class.java)
            .updateMemberInfo(
                MemberUpdateInfoRequest(
                    memberName = memberUpdateInfoRequest.memberName
                )
            )?.run {
                return when(this.code()) {
                    200 -> MemberState.Success(this.body() ?: MemberUpdateInfoResponse() )
                    401 -> MemberState.Unauthorized
                    403 -> MemberState.Forbidden
                    404 -> MemberState.NotFound
                    else -> MemberState.Error(java.lang.Exception("Uncaught Exception"))
                }
            } ?: return MemberState.Error(java.lang.Exception("Register Exception"))
    }
}