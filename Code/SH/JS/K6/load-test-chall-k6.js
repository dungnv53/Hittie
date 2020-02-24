import { group, sleep, check } from "k6";
import http from "k6/http";
import {Trend} from "k6/metrics";

// Create a custom metric to track latency and get login credentials from env vars
let latencyMetric = new Trend("latency");
let email = __ENV.EMAIL;
let password = __ENV.PASSWORD;

export let options = {
    // stages: [
    //     { duration: "1m", target: 10 },
    //     { duration: "1m", target: 20 },
    //     { duration: "2m", target: 50 },
    //     { duration: "2m", target: 80 },
    //     { duration: "2m", target: 110 },
    //     { duration: "3m", target: 120 },
    //     { duration: "5m", target: 140 },
    //     { duration: "3m", target: 150 },
    // ],
    stages: [
        { duration: "1m", target: 1 },
        { duration: "1m", target: 3 },
        { duration: "2m", target: 6 },
        { duration: "2m", target: 9 },
        { duration: "2m", target: 13 },
        { duration: "3m", target: 15 },
        { duration: "5m", target: 19 },
        { duration: "5m", target: 23 },
    ],
    thresholds: { "http_req_duration": [{ threshold: "p(95)<60000", abortOnFail: true }] } // 30s 60s
};

let optionsStag1 = {
  vus: 10,
  duration: "300s"
};

let optionsDo1 = {};

export default function() {
    // Login to get API session token
    // let res = http.post("http://kuailo-api-staging.krishna.us-west-1.elasticbeanstalk.com/wp-json/kuailo/v2.1/login",
    //     JSON.stringify({ email: 'lv+csm@krishnadigital.com', pass: 'dung#1234' }),
    //     { headers: { "Content-Type": "application/json" } }
    // );

    // Add custom metric data point and tag it with the endpoint (for graphing purposes)
    // latencyMetric.add(res.timings.waiting, {endpoint: "account/login"});
    sleep(1);

    // Extract API session token from JSON response
    // let resJson = res.json();  // res.json() is cached between calls
    // let apiToken = resJson.data.jwt;
    let apiToken = 'eyJhbGciOiJqd3QifQ.eyJpZCI6IjEiLCJmaXJzdG5hbWUiOiJEYXZlIiwibGFzdG5hbWUiOiJDdXJ0aXMiLCJlbWFpbCI6ImxldmkrY3NtQG9mZnNwcmluZ2RpZ2l0YWwuY29tIiwicmVnaXN0ZXJlZF9hdCI6IjIwMTMtMDctMjlUMDg6NDU6MDcrMDA6MDAiLCJnZW5kZXIiOiJNYWxlIiwiY291bnRyeV9jb2RlIjoiQVUiLCJkaWV0X3BsYW4iOiJtYWluc3RyZWFtIiwic3R1ZGlvIjoiRjQ1IERlZSBXaHkiLCJ0aW1lem9uZSI6IlVUQyIsImpvaW5fY2hhbGxlbmdlIjoiIn0.GbNzUnEp7Ey0CKA-kkkkkkkkhihihihih';
    // 14358
    let apiToken2 = 'eyJhbGciOiJqd3QifQ.eotuhoaesuahousth';
    // let staging_chall_api = 'http://kuailo-api-staging.kkkk.us-west-1.elasticbeanstalk.com';
    let staging_chall_api = 'http://www2-staging.kuailo.com';

    // console.log(apiToken);
    // Use session token to make API requests
    let res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/dashboard?date=2018-10-22&gender=m&country=AU&duo=mainstream",
        // { headers: { "Authorization": "Token " + apiToken } }
        { headers: { "Authorization": "Bearer " + apiToken } }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/dashboard"});
    sleep(3);

    // goal_points/weeks
    // TODO move to function, make random call order to avoid caching
    for(var i = 0; i < 3; i++) { // total 12 units (25req per 1 unit)
        getGoaldPointWeek(res, staging_chall_api, latencyMetric);
    }

    // meal_plans
    for(var i = 0; i < 5; i++) { // total 23
        getMealPlans(res, staging_chall_api, apiToken, latencyMetric);
    }

    // goal_points/date
    for(var i=0; i<3; i++) { // total 7
        getGoalPointsDate(res, staging_chall_api, apiToken, latencyMetric);
    }

    // body_stats
    for(var i = 0; i < 3; i++) { // total 10
        getBodyStats(res, staging_chall_api, apiToken, latencyMetric);
    }

    // chall
    for(var i = 0; i <= 3; i++) { // total 10
        getChallenge(res, staging_chall_api, apiToken, apiToken2, latencyMetric);
    }

    // POST goal_points/date
    postGoalPointsDate(res, staging_chall_api, latencyMetric);

    // TODO may be loop from a list of API
    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/chall",
        { headers: { "Authorization": "Bearer " + apiToken } }
    );

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/chall"});
    sleep(3);

    for(var i = 0; i < 3; i++) { // total 10
        getBodyStats(res, staging_chall_api, apiToken, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/chall_meta",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/chall_meta"});
    sleep(3);

    for(var i = 0; i < 3; i++) { // total 12 units (25req per 1 unit)
        getGoaldPointWeek(res, staging_chall_api, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/blog_posts",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });

    for(var i = 0; i < 4; i++) { // total 10
        getBodyStats(res, staging_chall_api, apiToken, latencyMetric);
    }

    latencyMetric.add(res.timings.waiting, {endpoint: "blog_posts"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/service_info",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "service_info"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/goals",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "goals"});
    sleep(3);

    for(var i=0; i<4; i++) { // total 6
        getGoalPointsDate(res, staging_chall_api, apiToken, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/faqs",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "faqs"});
    sleep(3);

    for(var i = 0; i <= 3; i++) { // total 10
        getChallenge(res, staging_chall_api, apiToken, apiToken2, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/recipes",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "recipes"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/recipes_updated",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "recipes_updated"});
    sleep(3);

    for(var i = 0; i < 3; i++) { // total 12 units (25req per 1 unit)
        getGoaldPointWeek(res, staging_chall_api, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/meal_plans/suppliers",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "suppliers"});
    sleep(3);

    for(var i = 0; i <= 4; i++) { // total 10
        getChallenge(res, staging_chall_api, apiToken, apiToken2, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/meal_plans/daily/2018-12-30",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "meal_plans"});
    sleep(3);

    for(var i = 0; i < 3; i++) { // total 23
        getMealPlans(res, staging_chall_api, apiToken, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/shopping_lists/weeks/3",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "shopping_lists"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/body_stats",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/body_stats"});
    sleep(3);

    getUserGoalPointWeek(res, staging_chall_api, apiToken2, latencyMetric);

    // TODO move to function for flexible random params
    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/goal_points/dates/2018-12-30",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/goal_points/dates"});
    sleep(3);

    for(var i = 0; i < 3; i++) { // total 12 units (25req per 1 unit)
        getGoaldPointWeek(res, staging_chall_api, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/8446/chall",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/id/chall"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/8446/shopping_lists/weeks/3",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/id/shopping_lists"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/8446/body_stats",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/id/body_stats"});
    sleep(3);

    for(var i = 0; i < 5; i++) { // total 23
        getMealPlans(res, staging_chall_api, apiToken, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/8446/goal_points/weeks/3",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/id/goal_points"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/8446/goal_points/dates/2018-12-30",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/id/goal_points/dates"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/timezones",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "timezones"});
    sleep(3);

    for(var i = 0; i < 5; i++) { // total 23
        getMealPlans(res, staging_chall_api, apiToken, latencyMetric);
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/countries",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "countries"});
    sleep(3);

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/studios",
        { headers: { "Authorization": "Bearer " + apiToken } }
        );

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "studios"});
    sleep(3);

    for(var i = 0; i < 5; i++) { // total 23
        getMealPlans(res, staging_chall_api, apiToken, latencyMetric);
    }
}

/**
 * Returns a random number between min (inclusive) and max (exclusive)
 */
function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}

/**
 * Returns a random integer between min (inclusive) and max (inclusive).
 * The value is no lower than min (or the next integer greater than min
 * if min isn't an integer) and no greater than max (or the next integer
 * lower than max if max isn't an integer).
 * Using Math.round() will give you a non-uniform distribution!
 */
function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function getGoaldPointWeek(res, staging_chall_api, latencyMetric) {
    // user 14358
    let apiToken2 = 'eyJhbGciOiJqd3QifQ.eyJpZCI6IjE0MzU4IiwiZmlyc3RuYW1lIjoiR29yZGFuYSIsImxhc3RuYW1lIjoiU3RlZmFub3Zza2kiLCJlbWFpbCI6ImxldmkrZ29yZGllc3RlZm9Ab2Zmc3ByaW5nZGlnaXRhbC5jb20iLCJyZWdpc3RlcmVkX2F0IjoiMjAxNS0wOS0wMVQwOToyMjoxMSswMDowMCIsImdlbmRlciI6IkZlbWFsZSIsImNvdW50cnlfY29kZSI6IkFVIiwiZGlldF9wbGFuIjoibWFpbnN0cmVhbSIsInN0dWRpbyI6IkY0NSBTaGVsbCBIYXJib3VyIiwidGltZXpvbmUiOiJBdXN0cmFsaWEvU3lkbmV5Iiwiam9pbl9jaGFsbGVuZ2UiOiIifQ.PtfeGWIHy7IhpiPqgbnlLZwCbbUPjqpmcWcjT7OuCUk';
    let week = getRandomInt(1, 8);
    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/goal_points/weeks/"+week,
    { headers: { "Authorization": "Bearer " + apiToken2 } } );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/goal_points"});
    sleep(3);
}

function getMealPlans(res, staging_chall_api, apiToken, latencyMetric) {
    let rand_date = getRandomInt(1, 30);
    if(rand_date < 10) {
        rand_date = "0"+ rand_date;
    }
    let rand_month = getRandomInt(1, 12);
    if(rand_month < 10) {
        rand_month = "0"+rand_month;
    }

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/meal_plans/daily/2018-"+rand_month + "-" +rand_date,
    { headers: { "Authorization": "Bearer " + apiToken } } );

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "meal_plans"});
    sleep(3);
}

function getGoalPointsDate(res, staging_chall_api, apiToken, latencyMetric) {
    let rand_month = getRandMonth();
    let rand_date = getRandDate();

    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/goal_points/dates/2018-"+rand_month+"-"+rand_date,
    { headers: { "Authorization": "Bearer " + apiToken } });
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/goal_points/dates"});
    sleep(3);
}

function getBodyStats(res, staging_chall_api, apiToken, latencyMetric) {
    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/body_stats",
    { headers: { "Authorization": "Bearer " + apiToken } }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/body_stats"});
    sleep(3);
}

function getChallenge(res, staging_chall_api, apiToken, apiToken2, latencyMetric) {
    // TODO add more random token
    var rand_api = getRandomInt(1,2);
    let tempToken = apiToken;
    if(rand_api == 2) {
        tempToken = apiToken2;
    }
    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/chall",
    { headers: { "Authorization": "Bearer " + tempToken } });

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/chall"});
    sleep(3);
}

function getUserGoalPointWeek(res, staging_chall_api, apiToken2, latencyMetric) {
    let rand_week = getRandomInt(1, 8);
    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/goal_points/weeks/"+rand_week,
    { headers: { "Authorization": "Bearer " + apiToken2 } }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/goal_points"});
    sleep(3);
}

function getAPIToken() {
    let index = getRandomInt(0, 3);
    let apiTokens = [
        'eyJhbGciOiJqd3QifQ.GbNzUnEp7Ey0CKAueoauoe', // lv
        'eyJhbGciOiJqd3QifQ.oeauoehus', // 8881 or 14358
        'eyJhbGciOiJqd3QifQ.ueoauths', // 9111
        'eyJhbGciOiJqd3QifQosauhoaesuth', // 8921
    ];

    return apiTokens[index];
}

function getRandDate() {
    let rand_date = getRandomInt(1,30);
    if(rand_date < 10) {
        rand_date = "0"+ rand_date;
    }
    return rand_date;
}

function getRandMonth() {
    let rand_month = getRandomInt(1, 12);
    if(rand_month < 10) {
        rand_month = "0"+rand_month;
    }
    return rand_month;
}

function postGoalPointsDate(res, staging_chall_api, latencyMetric) {
    let rand_month = getRandMonth();
    let rand_date = getRandDate();
    let apiToken = getAPIToken();

    let formdata = {
        goal_points: {"id": 1, "points": 15}
    };
    let headers = { "Content-Type": "application/x-www-form-urlencoded", "Authorization": "Bearer "+apiToken };
    res = http.post(staging_chall_api + "/wp-json/kuailo/v2.1/users/users/goal_points/dates/2018-"+rand_month + "-"+rand_date, formdata, { headers: headers });
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/chall"});
    sleep(3);
}
