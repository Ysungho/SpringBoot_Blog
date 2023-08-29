// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('삭제 실패했습니다.');
            location.replace('/articles');
        }

        httpRequest('DELETE',`/api/articles/${id}`, null, success, fail);
    });
}
// html에서 id를 delete-btn으로 설정한 엘리먼트를 찾아 그 엘리먼트에서 클릭 이벤트가 발생하면 fetch()메서드를 통해
// /api/articles/DELETE 요청을 보내는 역할을 한다.
// fetch()메서드에서 이어지는 then() 메서드는 fetch()가 잘 실행 되면 연이어 실행되는 메서드 이다.
// alert()메서드는 then() 메서드가 실행되는 시점에 웹 브라우저 화면으로 삭제가 완료되었음을 알리는 팝업을 띄워주는 메서드 이다.
// location.replace() 메서드는 실행 시 사용자의 웹 브라우저 화면을 현재 주소를 기반해 옮겨주는 역할을 한다.
// [삭제]버튼을 눌렀을 때 삭제하도록 button 엘리먼트에 delete-btn이 라는 아이디 값을 추가하고 article.js 가 동작하도록 import 한다.




// 수정 기능
//id가 modify-btn인 엘리먼트 조회
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {//클릭 이벤크가 감지되면 수정 API 요청 
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        })

        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/articles/${id}`);
        }

        function fail() {
            alert('수정 실패했습니다.');
            location.replace(`/articles/${id}`);
        }

        httpRequest('PUT',`/api/articles/${id}`, body, success, fail);
    });
}
//id가 modify-ben인 엘리먼트를 찾고 그 엘리먼트에서 클릭 이벤트가 발생하면
//id가 title, content인 엘리먼트의 값을 가져와 fetch() 메서드를 통해서 수정 API로 /api/articles/ PUT 요청을 보냅니다.
//요청을 보낼 때는 headers에 요청 형식을 지정하고, body에 html에 입력한 데이터를 json형식으로 바궈 보냅니다.
//요청이 완료되면 then()메서드로 마무리 작업을 합니다.




// 생성 기능: [등록]버튼을 누르면 입력 칸에 있는 데이터를 가져와 게시글 생성 api에 글 생성 관련 요청을 보내주는 코드
const createButton = document.getElementById('create-btn');//id가 create-btn인 엘리먼트

if (createButton) {
    // 클릭 이벤트가 발생하면 id가 tittle.content인 엘리먼트의 값을 가져와 fetch() 메서드를 통해 생성된 api로
    // /api/articles/POST 요청을 보냄

    // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다
    createButton.addEventListener('click', event => {
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        });
        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles');
        };
        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles');
        };

        httpRequest('POST','/api/articles', body, success, fail)
    });
}


// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}