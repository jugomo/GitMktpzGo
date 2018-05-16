package com.site11.jugomo.gitmktpzgo.Util

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class GraphqlAdapter {

    internal var AUTH_TOKEN = "***"
    internal var BASE_URL = "https://api.github.com/graphql"   /// <--- ESTA!!!
    internal var BASE_URL1 = "https://developer.github.com/v4/"
    internal var BASE_URL2 = "https://api.github.com/marketplace_listing/"

    var client: ApolloClient? = null
    var listener: ApolloCall.Callback<FindQuery.Data>? = null


    //
    //
    //


    constructor(listener: ApolloCall.Callback<FindQuery.Data>) {
        this.listener = listener
        client = setupApollo()
    }

    companion object {
        fun newInstance(listener: ApolloCall.Callback<FindQuery.Data>): GraphqlAdapter {
            var graphQLAdapter = GraphqlAdapter(listener)
            return graphQLAdapter
        }
    }

    public fun request(project: String, repository: String) {
        var fq = FindQuery.builder()
        fq.name(project)
        fq.owner(repository)
        var feedQuery = fq.build()
        var res = client!!.query(feedQuery)
        res.enqueue(listener)
    }

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor({ chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())

                    builder.addHeader("Authorization"
                            , "Bearer " + AUTH_TOKEN)

                    //builder.addHeader("Accpt", "application/vnd.github.valkyrie-preview+json")
                    //builder.addHeader("client_id","jugomo18")
                    //builder.addHeader("secret",AUTH_TOKEN)

                    chain.proceed(builder.build())
                })
                .build()
        return ApolloClient.builder()

                .serverUrl(BASE_URL)

                .okHttpClient(okHttp)
                .build()
    }

}