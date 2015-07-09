/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.query;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.support.QueryInnerHitBuilder;

import java.io.IOException;
import java.util.Objects;

public class NestedQueryBuilder extends AbstractQueryBuilder<NestedQueryBuilder> {

    public static final String NAME = "nested";

    private final QueryBuilder queryBuilder;

    private final String path;

    private String scoreMode;

    private QueryInnerHitBuilder innerHit;

    static final NestedQueryBuilder PROTOTYPE = new NestedQueryBuilder();

    public NestedQueryBuilder(String path, QueryBuilder queryBuilder) {
        this.path = path;
        this.queryBuilder = Objects.requireNonNull(queryBuilder);
    }

    /**
     * private constructor only used internally
     */
    private NestedQueryBuilder() {
        this.path = null;
        this.queryBuilder = null;
    }

    /**
     * The score mode.
     */
    public NestedQueryBuilder scoreMode(String scoreMode) {
        this.scoreMode = scoreMode;
        return this;
    }

    /**
     * Sets inner hit definition in the scope of this nested query and reusing the defined path and query.
     */
    public NestedQueryBuilder innerHit(QueryInnerHitBuilder innerHit) {
        this.innerHit = innerHit;
        return this;
    }

    @Override
    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(NAME);
        builder.field("query");
        queryBuilder.toXContent(builder, params);
        builder.field("path", path);
        if (scoreMode != null) {
            builder.field("score_mode", scoreMode);
        }
        printBoostAndQueryName(builder);
        if (innerHit != null) {
            builder.startObject("inner_hits");
            builder.value(innerHit);
            builder.endObject();
        }
        builder.endObject();
    }

    @Override
    public final String getName() {
        return NAME;
    }
}