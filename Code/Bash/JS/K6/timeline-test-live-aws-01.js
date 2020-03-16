import { group, sleep, check } from "k6";
import http from "k6/http";
import {Trend} from "k6/metrics";

// Create a custom metric to track latency and get login credentials from env vars
let latencyMetric = new Trend("latency");
let email = __ENV.EMAIL;
let password = __ENV.PASSWORD;

export let options = {
    stages: [
        { duration: "1m", target: 10 },
        { duration: "1m", target: 13 },
        { duration: "2m", target: 16 },
        { duration: "2m", target: 19 },
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
    // TODO investigate affect data, 3rd or abnormally outcome

    // Add custom metric data point and tag it with the endpoint (for graphing purposes)
    // latencyMetric.add(res.timings.waiting, {endpoint: "account/login"});
    sleep(1);

    // Extract API session token from JSON response
    // let resJson = res.json();  // res.json() is cached between calls
    // let apiToken = resJson.data.jwt;
    let apiToken = 'eyJhbGciOiJqd3QifQ.eusaouhs';
    // TODO random or list of big access_code list
    let access_code = 'k4m9';
    // let tlnr_api = 'http://f45-chlg-api-staging.wwmwxb3tt7.us-west-1.elasticbeanstalk.com';
    let tlnr_api = 'http://tlnr.api.krishna1.com';

    // console.log(apiToken);
    // Use session token to make API requests
    let res = http.get(tlnr_api + "/v/tlnr/" + access_code,
        // { headers: { "Authorization": "Token " + apiToken } }
        { headers: {} }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
    });

    latencyMetric.add(res.timings.waiting, {endpoint: "v tlnr"});
    sleep(3);

    res = http.get(tlnr_api + "/test/avatar_positions",
        { headers: {} }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
    });

    latencyMetric.add(res.timings.waiting, {endpoint: "test avatar"});
    sleep(1);

    postSaveLogs(res, tlnr_api, latencyMetric);

    res = http.get(tlnr_api + "/v22/tlnrfranchisee/"+ access_code,
        { headers: {} }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
    });

    latencyMetric.add(res.timings.waiting, {endpoint: "franchisee"});
    sleep(1);

    // TODO random type studio/...
    res = http.get(tlnr_api + "/v22/tlnrworkout/" + access_code +"/studio",
        { headers: {} }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
    });

    latencyMetric.add(res.timings.waiting, {endpoint: "workout"});
    sleep(1);


    res = http.get(tlnr_api + "/fm/" + access_code,
        { headers: {} }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
    });

    latencyMetric.add(res.timings.waiting, {endpoint: "fm"});
    sleep(1);

    // Err
    res = http.get(tlnr_api + "/9/tlnr/" + access_code,
        { headers: {} }
    );
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
    });

    latencyMetric.add(res.timings.waiting, {endpoint: "9/tlnr"});
    sleep(1);

    // goal_points/weeks

    // meal_plans
    for(var i = 0; i <= 22; i++) { // total 23
        res = http.get(tlnr_api + "/v"+i+ "/tlnr/" + access_code,
        // { headers: { "Authorization": "Token " + apiToken } }
        { headers: {} }
        );
        check(res, {
            "status is 200": (res) => res.status === 200,
            "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
        });

        latencyMetric.add(res.timings.waiting, {endpoint: "v"+i+ " tlnr"});
        sleep(1);
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
    res = http.get(staging_chall_api + "/wp-json/kuailo/v2.1/users/chlg",
    { headers: { "Authorization": "Bearer " + tempToken } });

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/chlg"});
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
        'eyJhbGciOiJqd3QifQ.eutaoshug', // lvl
        'eyJhbGciOiJqd3QifQ.eyJpZCIl9jaGFsbGVuZ2UiOiIifQ.PtfeGWIHy7IhpiPqgbnlLZwCbbUPjqpmcWcjT7OuCUk', // 8881 or 14358
        'eyJhbGciOiJqd3QifQ.eyJpZCIiZbgNs5sx4b2j78hQYEeYP_3espex3maUYKafT-r0', // 9111
        'eyJhbGciOiJqd3QifQ.eyJpZCIFdsf1fBAckFkVALiQVh6NhZMr7bTD6pPriS13KHDuCs', // 8921
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

function postSaveLogs(res, tlnr_api, latencyMetric) {
    let version = getRandomInt(3, 7);

    let formdata = {
        "gym_id": 401, "message": "settimerupdate-true-1414559305463-false-0", "screen": 0, "sent_at": ""
    };
    let headers = { "Content-Type": "application/x-www-form-urlencoded" };
    res = http.post(tlnr_api + "/v"+ version + "/mstyii_logs/save/"+"yii_id=401&message=settimerupdate-true-1414559305463-false-0&screen=0&sent_at=", formdata, { headers: headers });

    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('success')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "save logs"});
    sleep(3);
}

function postGoalPointsDate(res, staging_chall_api, latencyMetric) {
    let rand_month = getRandMonth();
    let rand_date = getRandDate();
    let apiToken = getAPIToken();

    let formdata = {
        goal_points: {"id": 1, "points": 15}
    };
    let headers = { "Content-Type": "application/x-www-form-urlencoded", "Authorization": "Bearer "+apiToken };
    res = http.post(staging_chall_api + "/wp-json/kuailo/v2.1/users/users/_points/dates/2018-"+rand_month + "-"+rand_date, formdata, { headers: headers });
    check(res, {
        "status is 200": (res) => res.status === 200,
        "content OK": (res) => JSON.parse(res.body).hasOwnProperty('status')
    });
    latencyMetric.add(res.timings.waiting, {endpoint: "users/chlg"});
    sleep(3);
}
