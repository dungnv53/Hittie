@extends('layouts.front_in')

@section('script')
    @include('front._shared.js_load_more_pages', ['classNames'=>['accept', 'popularEntry'], 'nums' => [3, 3], 'pages' => [PAGER_SIZE_FRONT, PAGER_SIZE_FRONT], 'status' => ['entry', 'entry']])
    @include('front._shared.js_load_more_pages', ['classNames'=>['popularEntry'], 'nums' => [NUM_FIRST_PAGE_10], 'pages' => [PAGER_SIZE_FRONT], 'status' => ['entry']])
    {{ HTML::script('common/front/js/tab_change.js') }}
@stop

@section('content')
    @include('layouts.elements.displays.feature_box')
    <section class="l-margin">
        <div class="search">
            <p class="redFont" style="display:none">{{ W_FRM_REQUIRED_KEYWORD }}</p>
            <form id="head_search_key" method="GET" action="{{ route('search.result') }}">
                <ul>
                <li class="search-key"><input type="text" name="head_search_key" placeholder="キーワードを入力"></li>
                <li class="search-bt"><input class="submit" type="submit" name="" value="検索" onclick="validateSearch(head_search_key)"></li>
                </ul>
            </form>
        </div><!--search-->
    </section>

    <section class="entryResultBox">
        <div class="box-tabs">
        <ul class="tabs">
            <li class="active"><a href="#"><span class="iconEntry">受付中</span></a></li>
            <li><a href="#"><span class="iconResult">結果発表</span></a></li>
        </ul>
        </div><!--box-tabs-->
        
        <div class="tabs_content">
            <div class="entryBox" style="display: block;">
            <div class="box">
                <div class="keywords">
                    <h3>注目キーワード：</h3>
                    <ul class="words">
                        <li><a href="{{ route('list.tag.entry', isset($pop_quest_ready->id)? 1 : 0 ) }}">{{ $pop_quest_ready->title or 'null'}}
                        @foreach($tab_one as $attention) 
                        <li><a href="{{ route('list.tag.entry', $attention->keyword->id) }}">{{ $attention->keyword->keyword_name }}</a></li>
                        @endforeach
                    </ul>
                </div>
               
                <a href="#">
                <div class="pushContents">
                    <h3>男性の方へのアンケート募集中！</h3>
                     
                     <div class="pushInfo">
                       <p class="pushText">彼女のイヤな仕草は？</p>
                    </div>
                    
                </div><!--pushContents-->
                </a>
            </div><!--box-->
            
            <h3 class="listSectionTit"><span>ピックアップ</span></h3>
            <h3 class="listSectionTit"><span>新着　受付中</span></h3>
            <div class="links">
            <ul>
                <li>
                    <a href="#">
                    <span class="text">家に帰るとまず何をする？</span>
                    <span class="date">2014年10月7日 12:00</span></a>
                </li>
                <li>
                    <a href="#">
                    <span class="text">家に帰るとまず何をする？</span>
                    <span class="date">2014年10月7日 12:00</span></a>
                </li>
            </ul>
            </div><!--links-->

            </div><!--entryBox-->
        
            <div class="resultBox" style="display: block;">
                <!-- none -->
            <div class="box">
                <div class="keywords">
                    <h3>注目キーワード：</h3>
                    <ul class="words">
                        <li><a href="#">クリスマス</a></li>
                        @foreach($tab_two as $attention) 
                        <li><a href="{{ route('list.tag.entry', $attention->keyword->id) }}">{{ $attention->keyword->keyword_name }}</a></li>
                        @endforeach
                    </ul>
                </div>
               
               <a href="#">
                <div class="pushContents">
                    <h3>30代男性が興味あるアンケートは？</h3>
                     <div class="pushInfo">
                       <p class="pushText">あなたがハマったスポ根ドラマは？</p>
                    </div>
                </div><!--pushContents-->
                </a>
            </div><!--box-->
            
            <h3 class="listSectionTit"><span>ピックアップ</span></h3>
            
            @include('front._shared._question_list', ['className' => 'accept', 'displaySeeMore' => TRUE])

            @include('front._shared._link_to_list', ['text' => '未回答一覧', 'route' => 'list.unanswered'])
            
            </div><!--resultBox-->
        
        </div><!--tab_content-->

        <div class="links">
            <ul id="popularEntry"></ul>
            @include('front._shared._question_list', ['displaySeeMore' => TRUE, 'className' => 'popularEntry' ])
        </div>
        
    </section><!--entryResultBox-->

    @include('layouts.front_elements._profileBn')

    <section class="l-margin">
        <h3 class="subSectionTit evaluateIcon">みんなのアンケート評価</h3>
        
        <ul class="tabs02">
            <li class=""><a href="#">{{ HTML::image('common/front/images/sp/evaluate01.png') }}</a></li>
            <li class=""><a href="#">{{ HTML::image('common/front/images/sp/evaluate02.png') }}</a></li>
            <li class="active"><a href="#">{{ HTML::image('common/front/images/sp/evaluate03.png') }}</a></li>
        </ul>
        
        <div class="tabs_content02">
        <div class="links noBorder" style="display: none;">
        <ul>
            <li>
                <a href="#"><span class="text">家に帰るとまず何をする？</span>
                <span class="date">2014年10月7日 2:00</span></a>
            </li>
        </ul>
        </div><!--links-->
        
        <div class="links noBorder" style="display: none;">
        <ul>
            <li>
                <a href="#"><span class="text">家に帰るとまず何をする？2</span>
                <span class="date">2014年10月7日 13:00</span></a>
            </li>
        </ul>
        </div><!--links-->

        <div class="links noBorder" style="display: block;">
        <ul>
            <li>
                <a href="#"><span class="text">家に帰るとまず何をする？3</span>
                <span class="date">2014年10月7日 12:00</span></a>
            </li>
        </ul>
        </div><!--links-->
        </div>

    </section>

    <section class="l-margin">
        <h3 class="subSectionTit columnIcon">コラム</h3>
        <div class="links noBorder">
        <ul>
            <li>
                <a href="#"><span class="text">コラムタイトル</span>
                <span class="date">2014年10月7日 12:00</span></a>
            </li>
            <li>
                <a href="#"><span class="text">コラムタイトル</span>
                <span class="date">2014年10月7日 12:00</span></a>
            </li>
            <li>
                <a href="#"><span class="text">コラムタイトル</span>
                <span class="date">2014年10月7日 12:00</span></a>
            </li>
        </ul>
        </div>
        <p class="gotoListArea"><a href="#">過去のコラムを見る</a></p>
    </section>
        

    <script>
    $(document).ready(function(){
        $("ul.tabs").tabChange({content: ".tabs_content", animate: true});  
        $("ul.tabs02").tabChange({content: ".tabs_content02", animate: true});
    });
    </script>

@stop