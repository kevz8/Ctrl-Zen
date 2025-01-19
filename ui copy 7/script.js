let stream;
let captureInterval;
let eventLog = [];

/* Timer-related variables */
let timerInterval; 
let seconds = 0; 
let isPaused = false;

document.getElementById('startButton').addEventListener('click', startSession);
document.getElementById('stopButton').addEventListener('click', stopSession);
document.getElementById('pauseButton').addEventListener('click', togglePause);

async function startSession() {
  const video = document.getElementById('video');
  try {
    // Clear old event log
    eventLog = [];
    document.getElementById('eventlog').innerHTML = '';
    document.querySelector('#report p').textContent = 'Session started...';

    // Start webcam
    stream = await navigator.mediaDevices.getUserMedia({ video: true });
    video.srcObject = stream;

    // Start capturing frames
    startCapturingFrames();

    // Reset and start the timer
    startTimer();

  } catch (err) {
    console.error('Error accessing webcam:', err);
    document.querySelector('#report p').textContent = 'Error accessing webcam';
  }
}

function stopSession() {
  if (stream) {
    const tracks = stream.getTracks();
    tracks.forEach(track => track.stop());
    document.getElementById('video').srcObject = null;
    clearInterval(captureInterval);

    // Stop the timer
    stopTimer();

    document.querySelector('#report p').textContent = 'Session ended.';
  }
}

/* 
  Frame capture logic 
  (unchanged except we call setInterval with analyzeFrame)
*/
function startCapturingFrames() {
  captureInterval = setInterval(analyzeFrame, 100);
}

async function analyzeFrame() {
  try {
    const video = document.getElementById('video');
    if (!video || video.videoWidth === 0 || video.videoHeight === 0) return;

    const canvas = document.createElement('canvas');
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    const context = canvas.getContext('2d');
    context.drawImage(video, 0, 0, canvas.width, canvas.height);

    const dataUrl = canvas.toDataURL('image/jpeg');
    const blob = await (await fetch(dataUrl)).blob();

    const formData = new FormData();
    formData.append('frame', blob);

    const response = await fetch('http://127.0.0.1:5000/analyze', {
      method: 'POST',
      body: formData
    });
    const result = await response.json();

    if (result.status === 0) {
      console.error(result.message);
      document.querySelector('#report p').textContent = result.message;
    } else {
      const { datetime, emotion, eye } = result.data;
      document.querySelector('#report p').textContent =
        `Emotion: ${emotion} | Eye: ${eye}`;

      eventLog.push({ datetime, emotion, eye });
      const logLine = document.createElement('p');
      logLine.textContent = `[${datetime}] Emotion: ${emotion}, Eye: ${eye}`;
      document.getElementById('eventlog').appendChild(logLine);
    }
  } catch (error) {
    console.error('Error:', error);
    document.querySelector('#report p').textContent = 'Error fetching data';
  }
}

/* 
  TIMER LOGIC
*/

/* Called when we click "start" session */
function startTimer() {
  stopTimer();  // in case it was running
  isPaused = false;
  seconds = 0;
  updateTimerDisplay(0);
  timerInterval = setInterval(() => {
    if (!isPaused) {
      seconds++;
      updateTimerDisplay(seconds);
    }
  }, 1000);
}

/* Called when we click "stop" session */
function stopTimer() {
  clearInterval(timerInterval);
  isPaused = false;
  updateTimerDisplay(seconds);
}

/* Called when we click the "pause/unpause" button */
function togglePause() {
  isPaused = !isPaused;
  const pauseBtn = document.getElementById('pauseButton');
  if (isPaused) {
    pauseBtn.querySelector('.my-4').textContent = 'unpause';
    pauseBtn.querySelector('.text-wrapper-2').textContent = 'timer';
  } else {
    pauseBtn.querySelector('.my-4').textContent = 'pause';
    pauseBtn.querySelector('.text-wrapper-2').textContent = 'timer';
  }
}

/* Format the timer text and set #timerText */
function updateTimerDisplay(sec) {
  const timerTextElem = document.getElementById('timerText');
  const m = Math.floor(sec / 60);
  const s = sec % 60;
  const sString = s < 10 ? '0' + s : s;
  timerTextElem.textContent = `${m}:${sString}`;
}
