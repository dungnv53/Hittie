<?php

namespace app\controllers;

use yii\authclient\clients\GoogleOAuth;
use Google_Client;
use Google_Service_Calendar;
use Google_Service_Calendar_Event;
use Google_Service_Calendar_EventDateTime;
use Google_Service_Calendar_EventAttendee;
use app\models\CalendarEventRecord;
use app\models\TaskDetailRecord;
use app\models\AppointmentRecord;
use app\models\PartnerEmployeeRecord;
use app\models\ApprovalRecord;
use yii\console\Controller;
use Yii;
use yii\web\Response;
use yii\filters\AccessControl;

use app\common\UploadHandler;
use app\common\TaskUploadHandler;
use app\models\TaskAttachFileRecord;
use app\models\EmployeeRecord;

class CalendarController extends \app\controllers\BaseController
{

    const CALENDAR_URL = "https://www.googleapis.com/calendar/v3/calendars/";

    public function behaviors()
    {
        return [
            'access' => [
                'class' => AccessControl::className(),
                'only' => ['index'],
                'rules' => [
                    [
                        'allow' => true,
                        'roles' => ['@'],
                        'denyCallback' => [$this, 'authenticationFailed'],
                    ],
                ],
            ],
        ];
    }

	public function actionIndex()
	{
        $this->page = 'calendar';
		$calendarId = Yii::$app->user->identity->email;
		//$event = static::createTestParams();
		//$simple_obj = json_encode($event->toSimpleObject());
		//$array_param = json_decode($simple_obj, true);
		//$array_param['is_json'] = true;
		//$createdEvent = static::insertEvent($calendarId, $array_param);
		$params = [];
		try {
			//$items = static::listEvents($calendarId, $params);
			//$items = CalendarEventRecord::getCalendars();
			$items = TaskDetailRecord::getTaskDetailCalendars();
			$actual = TaskDetailRecord::getTaskDetailCalendarsActual();

			$employees  = PartnerEmployeeRecord::query_all("employee", "employee_id, employee_name");
			$employee = [];
			foreach($employees as $row)
				$employee[$row['employee_id']] = $row['employee_name'];

			return $this->render('index', ['items' => json_encode($items), 'actual' => json_encode($actual), 'employees' => $employee]);
		} catch (Exception $e)
		{
			static::auth();
		}
	}

	private static function auth()
	{
		$google_oauth = Yii::$app->authClientCollection->getClient('google');
		$code = Yii::$app->request->get('code');
		if (isset($code)) 
		{
			$accessToken = $google_oauth->fetchAccessToken($code);
		} else 
		{
			Yii::$app->getResponse()->redirect('/site/auth?authclient=google');
		}
	}

	public static function listEvents($calendarId, $params)
	{
		try {
			$fetch_url = static::CALENDAR_URL . $calendarId . '/' . 'events';
			return static::request($fetch_url, 'GET', $params = $params);
		} catch (Exception $e)
		{
			throw $e;
		}
	}

	public static function getEvent($calendarId, $params)
	{
		if (array_key_exists('eventId', $params) && isset($params['eventId'])) 
		{
			try {
				$eventId = $params['eventId'];
				unset($params['eventId']);
				$fetch_url = static::CALENDAR_URL . $calendarId . '/events/' . $eventId;
				return static::request($fetch_url, 'GET', $params = $params);
			} catch (Exception $e)
			{
				throw $e;
			}
		}
	}

	public static function deleteEvent($calendarId, $params)
	{
		if (array_key_exists('eventId', $params) && isset($params['eventId']))
		{
			try {
				$eventId = $params['eventId'];
				unset($params['eventId']);
				$fetch_url = static::CALENDAR_URL . $calendarId . '/events/' . $eventId;
				
				return static::request($fetch_url, 'DELETE', $params = $params);
			} catch (Exception $e)
			{
				throw $e;
			}
		}
	}

	public static function updateEvent($calendarId, $params)
	{
		if (array_key_exists('eventId', $params) && isset($params['eventId']))
		{
			try {
				$eventId = $params['eventId'];
				unset($params['eventId']);
				$fetch_url = static::CALENDAR_URL . $calendarId . '/events/' . $eventId;
				$params['is_json'] = 1;
				return static::request($fetch_url, 'PUT', $params = $params);
			} catch (Exception $e)
			{
				throw $e;
			}
		}
	}

	public static function insertEvent($calendarId, $params)
	{
		try {
			$fetch_url = static::CALENDAR_URL . $calendarId . '/' . 'events';
			$headers = [
				'Content-Type: application/json; charset=UTF-8',
			];
			$params['is_json'] = 1;
			return static::request($fetch_url, 'POST', 
								   $params = $params,
								   $headers = $headers);
		} catch (Exception $e)
		{
			throw $e;
		}
	}

	public static function request($fetch_url, $method, array $params = [], array $headers = [])
	{
		try {
			$google_oauth = Yii::$app->authClientCollection->getClient('google');
			$accessToken = $google_oauth->getAccessToken();
			$rf_token	=	$accessToken->getParam('refresh_token');
			if (!is_object($accessToken) || !$accessToken->getIsValid() || ($accessToken->getIsExpired() && empty($rf_token))) {
				static::auth();
			} else {
				$results = $google_oauth->api($fetch_url,
										  $method,
										  $params = $params, 
										  $headers = $headers);
				return $results;
			}            
		} catch (Exception $e) 
		{
			throw $e;
		}
	}

	private static function createTestParams()
	{
		$event = new Google_Service_Calendar_Event();
		$event->setSummary("購買テストミーティング");
		$event->setLocation("購買テスト場所");
		$start = new Google_Service_Calendar_EventDateTime();
		$start->setDateTime('2015-04-26T10:00:00.000+07:00');
		$event->setStart($start);
		$end   = new Google_Service_Calendar_EventDateTime();
		$end->setDateTime('2015-05-01T10:00:00.000+07:00');
		$event->setEnd($end);
		$attendee = new Google_Service_Calendar_EventAttendee();
		$attendee->setEmail("bao@vn.sateraito.co.jp");
		$attendees = [ $attendee ];
		$event->attendees = $attendees;

		return $event;
		
	}

	public static function getGmailClient()
	{
		$google_oauth = Yii::$app->authClientCollection->getClient('google');
		$json_config = [
			"web" => [
				'client_id'  => $google_oauth->clientId,
				'client_secret' => $google_oauth->clientSecret,
			],
		];
		$client = new Google_Client();
		$client->setAuthConfig(json_encode($json_config));
		$client->addScope(Google_Service_Calendar::CALENDAR);
		$client->setAccessToken(json_encode($google_oauth->getAccessToken()->getParams()));
		return $client;
	}

	public static function getEvents($calendarId, $params, $client) 
	{
		$service = new Google_Service_Calendar($client);
		$results = $service->events->listEvents($calendarId, $params);
		$items = [];
		if (count($results->getItems()) > 0) 
		{
			foreach($results->getItems() as $item) 
			{
				array_push($items, $item);
			}
		}

		return $items;
	}

	public function actionSave()
	{
		return true;
	}

	public function actionInsert()
	{
		$response = Yii::$app->response;
		if (Yii::$app->request->isAjax) {
			$response->format = Response::FORMAT_JSON;
			$title 			= Yii::$app->request->post('title');
			$description 	= Yii::$app->request->post('description');
			$progress 		= Yii::$app->request->post('progress');
			$start 			= Yii::$app->request->post('start');
			$end 			= Yii::$app->request->post('end');
			$counter = 0;
			$errors = [];
			$saved_data = [];
			$filter_row = [
				'task_name' 	=> $title,
				'ask_date' 		=> $start,
				'due_date' 		=> $end,
				'progress_int' 	=> $progress,
			];

			$model = new TaskDetailRecord($filter_row);
			if ($model->save())
			{
				$counter += 1;
			}
			else
			{
				array_push($errors, $model->getErrors());
			}

			$response->data = [
				'count' => $counter,
				'sucess' => true,
				'errors' => $errors,
			];

		} else {
			$response->statusCode = 400;
		}

		return $response;
	}

	public function actionGetenddate()
	{
		$response = Yii::$app->response;
		if (Yii::$app->request->isAjax) {
			$response->format = Response::FORMAT_JSON;
			
			$event_id	= Yii::$app->request->post('event_id');
			$start_time = Yii::$app->request->post('start_time');
			$execute_time = Yii::$app->request->post('execute_time');
			
			$employee = TaskDetailRecord::getWorkTime($event_id);
			
			$work_start = $employee['work_start_time'];
			$work_end = $employee['work_end_time'];
			$lunch_start = $employee['lunch_start_time'];
			$lunch_end = $employee['lunch_end_time'];
						
			$end_date = "";
			
			$end_date = ProjectManagementController::calcEndTime($start_time, $execute_time, $work_start, $work_end, $lunch_start, $lunch_end);
			$end_date .= ":00";
			$response->data = [
					'end_date' => $end_date,
			];
		} else
			$response->statusCode = 400;
	
		return $response;
	}

	public function actionRefresh()
	{
		$response = Yii::$app->response;
		if (Yii::$app->request->isAjax) {
			$response->format = Response::FORMAT_JSON;
			$employee_id 		= Yii::$app->request->post('employee_id');

			$events = TaskDetailRecord::getTaskDetailCalendars();
			$actual = TaskDetailRecord::getTaskDetailCalendarsActual();

			$response->data = [
				'events' => $events,
				'actual' => $actual,
			];
		} else
			$response->statusCode = 400;

		return $response;
	}

	public function actionUpdate()
	{
		$response = Yii::$app->response;
		if (Yii::$app->request->isAjax) {
			$response->format = Response::FORMAT_JSON;
			$id 					= 	Yii::$app->request->post('id');
			$title 					= 	Yii::$app->request->post('title');
			$description 			= 	Yii::$app->request->post('description');
			$progress 				= 	Yii::$app->request->post('progress');
			$start 					= 	Yii::$app->request->post('start');
			$end 					= 	Yii::$app->request->post('end');
			$employee_id 			= 	Yii::$app->request->post('employee_id');
			$actual_start			= 	Yii::$app->request->post('actual_start');
			$actual_end 			= 	Yii::$app->request->post('actual_end');
			$actual_execute_time	=	Yii::$app->request->post('actual_execute_time');
			$executetime_input_type			=	Yii::$app->request->post('executetime_input_type');

			$counter = 0;
			$sucess = false;
			$errors = [];
			$saved_data = [];
			$filter_row = [
				'id' 					=> $id,
				'task_name' 			=> $title,
				'ask_date' 				=> $start,
				'due_date' 				=> $end,
				'actual_ask_date'		=> $actual_start,
				'actual_due_date'		=> $actual_end,
				'actual_execute_time'	=> $actual_execute_time,
				'executetime_input_type'=> $executetime_input_type,
				'progress_int' 			=> $progress,
			];
			
			$model = TaskDetailRecord::findOne($id);
			if ($model)
			{	
				//get event old of TaskDetailRecord
				$event_old = TaskDetailRecord::getOne($id);

				foreach ($filter_row as $key => $value)
				{
					if (isset($value) && $value !== null)
						$model->$key 		= $value;
				}

				if ($model->save())
					$sucess = true;
				else
					array_push($errors, $model->getErrors());

				if ($sucess == true && (int)$progress == 100)
				{
					$progress_old = (int)$event_old['progress_int'];
					if($progress_old != 100){
						$this->sendMailReport($employee_id, $filter_row);

						// get direct boss
						$employee = AppointmentRecord::getOneEmailEmployee($employee_id);
						$boss_id = $employee['boss_id'];
						
						if($boss_id!=""){
							$approve_data = [
								'task_detail_id' => $filter_row['id'],
								'boss_id' => $boss_id,
								'approve_flag' => 0,
								'reason' => ''
							];
							$approval_model = new ApprovalRecord($approve_data);
				            if (!($approval_model->save())) {
								array_push($errors, $approval_model->getErrors());
				            }
						}						
					}
				}
			}

			$events = TaskDetailRecord::getTaskDetailCalendars();
			$actual = TaskDetailRecord::getTaskDetailCalendarsActual();

			$response->data = [
				'sucess' => $sucess,
				'errors' => $errors,
				'events' => $events,
				'actual' => $actual,
			];
		} else
			$response->statusCode = 400;

		return $response;
	}

	public function sendMailReport($employee_id, $filter_row)
	{
		// get list email boss
		$emails = AppointmentRecord::getOneEmailEmployee($employee_id);
		
		//send email to person incharge if email is not null
		$employee_name	=	$emails['employee_name'];
		$boss_email = $emails['boss_email'];

		if(!empty($boss_email)){
			Yii::$app->mailer->compose('@app/mail/mail-report', ['rows' => $emails])
            ->setFrom([Yii::$app->mailer->getTransport()->getUsername() => "Report mail"])
            ->setTo($boss_email)
            ->setSubject('you got application from '.$employee_name)
            //->setBcc('')
            ->send();
		}
	}

	public function actionAjaxUpload()
	{

		$request = './request.txt';
		$ln = "\n\r";
		// file_put_contents($myfile, " des: ". json_encode(Yii::$app->request->get()));
		file_put_contents($request, " des: ". json_encode($_REQUEST). date('H-i-s') . $ln, FILE_APPEND);
		$task_id = isset($_GET['task_id']) ? $_GET['task_id'] : null; // null~430 fix me
		// isset($_REQUEST['task_id']) ? $_REQUEST['task_id']
		// $task_id = 430;
		$task_upload = new TaskAttachFileRecord();
		$current_user = EmployeeRecord::getOnline();
		$options = ['model' => $task_upload, 'task_id' => $task_id, 'created_by' => $current_user];
		$upload_handler = new TaskUploadHandler($options);
		exit;
	}

	public function actionAjaxDelTaskFile()
	{
		// preventNonAjax()
		$task_id = Yii::$app->request->get('task_id');
		if($task_id) {
			$current_task_attach = TaskAttachFileRecord::findOne($task_id);
			if(count($current_task_attach) > 0) {
				$current_task_attach->delete_flg = 1;
				$current_task_attach->save();

				echo json_encode(['data' => 'success']);
				exit;
			}
		}

		echo json_encode(['data' => '']);
		exit;
	}

}