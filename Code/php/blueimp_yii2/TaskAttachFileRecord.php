<?php

namespace app\models;

use Yii;
use yii\db\Query;
use yii\behaviors\TimestampBehavior;
use yii\db\ActiveRecord;

/**
 * This is the model class for table "task".
 *
 * @property integer $id
 * @property string $name
 * @property string $description
 * @property string $creation_time
 * @property string $update_time
 * @property integer $delete_flag
 */
class TaskAttachFileRecord extends \yii\db\ActiveRecord implements LogInterface
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'task_attach_file';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['created_date', 'updated_date'], 'safe'],
            [['delete_flg'], 'integer'],
            [['display_name'], 'string', 'max' => 255]
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'description' => '説明',
            'created_date' => '生成日',
            'updated_date' => '更新日',
            'delete_flg' => '削除フラグ',
        ];
    }

    /**
     * @inheritdoc
     */
    public function beforeDelete()
    {
        if (parent::beforeDelete())
        {
            LogRecord::logBeforeDelete('task', $this);
            return true;
        } 
        else 
        {
            return false;
        }
    }

    /**
     * @inheritdoc
     */

    public function afterSave($insert, $changedAttributes)
    {
        parent::afterSave($insert, $changedAttributes);
        LogRecord::logAfterSave($insert, $changedAttributes, 'task', $this, 'app\models\TaskRecord');
    }

    /**
     * @inheritdoc 
     */
    public static function logAttributes()
    {
        return [
            'id' => 'ID',
            'created_at' => '生成日',
            'updated_at' => '更新日',
            'delete_flg' => '削除フラグ',
        ];
    }

    /**
     * @inheritdoc
     */
    public function logIdentifier()
    {
        return $this->id;
    }

    /**
     * @inheritdoc
     */
    public function behaviors()
    {
        return [
            [
            'class'  => TimestampBehavior::className(),
            'attributes' => [
                ActiveRecord::EVENT_BEFORE_INSERT => ["updated_date", 'created_date'],
                ActiveRecord::EVENT_BEFORE_UPDATE => ["updated_date"],
            ],
            'value' => new \yii\db\Expression('NOW()'),
            ],
            [
                'class'  => \yii\behaviors\AttributeBehavior::className(),
                'attributes' => [
                    ActiveRecord::EVENT_BEFORE_INSERT => ['delete_flg'],
                ],
                'value' => function ($event) {
                    return 0;
                },
            ]
        ];
    }

    public function displayLabels()
    {
        return [
            'id' => 'ID',
        ];
    }

    public static function getOne($id)
    {
        $data = static::find()->where(['delete_flg' => 0, 'id' => $id])->asArray()->one();
        return $data;
    }

    public static function getAll()
    {
        $data = static::find()->where(['delete_flg' => 0])->asArray()->all();

        return $data;
    }
    
    public function keyFormats()
    {
		return [
           
		];
    }

    public static function columnsConnection()
    {
        return [];
    }

    public function test()
    {
        return 'du';
    }

    public function getAttachment()
    {
        return ['data' => 'kkk'];
    }

    public function getAttachmentByTaskDetail($task_detail_id = null)
    {
        $data = static::find()->where(['delete_flg' => 0, 'task_detail_id' => $task_detail_id])->asArray()->all();

        return $data;
    }

    /**
     * format data follow blueimp json format
     */
    public function formatUploadedData($uploaded)
    {
        $result = [];
        $app_url = 'http://gyoumukanri-test.org';
        $base_url = self::get_full_url();
        foreach($uploaded as $file) {
            // TODO testing realpath() on linux
            // $basePath = Yii::$app->basePath;
            $basePath = '';// 'http://gyoumukanri-test.org';
            $uploadPath = '/uploads/tasks/'; // fix me
            $task_id = $file['task_detail_id']; // fix me

            $tmp['name'] = $file['filename'];
            $tmp['size'] = $file['size'];
            // $result[]['url']  = $basePath. $uploadPath. $task_id ."/" . $file['filename'];
            $tmp['url']  = $base_url .$uploadPath. $task_id. "/". $file['filename'];
            $tmp['thumbnailUrl'] = $uploadPath. $task_id. "/thumbnail/" . $file['filename'];
            $tmp['deleteUrl'] = $base_url. '/calendar/ajax-del-task-file?task_id='. $file['id'];
            $tmp['deteteType'] = 'DELETE';

            $result[] = $tmp;
        }

        return $result;
    }

    public function deleteTaskAttachment()
    {

    }

    protected function get_full_url() {
        $https = !empty($_SERVER['HTTPS']) && strcasecmp($_SERVER['HTTPS'], 'on') === 0 ||
            !empty($_SERVER['HTTP_X_FORWARDED_PROTO']) &&
                strcasecmp($_SERVER['HTTP_X_FORWARDED_PROTO'], 'https') === 0;
        return
            ($https ? 'https://' : 'http://').
            (!empty($_SERVER['REMOTE_USER']) ? $_SERVER['REMOTE_USER'].'@' : '').
            (isset($_SERVER['HTTP_HOST']) ? $_SERVER['HTTP_HOST'] : ($_SERVER['SERVER_NAME'].
            ($https && $_SERVER['SERVER_PORT'] === 443 ||
            $_SERVER['SERVER_PORT'] === 80 ? '' : ':'.$_SERVER['SERVER_PORT']))).
            substr($_SERVER['SCRIPT_NAME'],0, strrpos($_SERVER['SCRIPT_NAME'], '/'));
    }
}
