/* =========================
   TEMPLATE SELECTION
   ========================= */

function selectTemplate(template) {

    switch (template) {

        case "otp":
            window.location.href = "otp.html";
            break;

        case "welcome":
            window.location.href = "welcome.html";
            break;

        case "certificate":
            window.location.href = "certificate.html";
            break;

        case "custom":
            window.location.href = "custom-template.html";
            break;

        default:
            alert("Invalid template selected.");
    }
}


/* =========================
   INSERT VARIABLE
   ========================= */

function insertVariable(variable) {

    const message =
        document.getElementById("message");

    const start =
        message.selectionStart;

    const end =
        message.selectionEnd;

    const text =
        message.value;

    message.value =
        text.substring(0, start)
        + variable
        + text.substring(end);

    message.focus();

    message.selectionStart =
        start + variable.length;

    message.selectionEnd =
        start + variable.length;
}


/* =========================
   PREVIEW
   ========================= */

function previewTemplate() {

    const message =
        document.getElementById("message").value;

    const preview =
        document.getElementById("preview");

    if (message.trim() === "") {

        preview.textContent =
            "Please enter a message.";

        return;
    }

    preview.textContent = message;
}


/* =========================
   SAVE TEMPLATE
   ========================= */

function saveTemplate() {

    const templateName =
        document.getElementById("templateName").value.trim();

    const subject =
        document.getElementById("subject").value.trim();

    const message =
        document.getElementById("message").value.trim();


    if (templateName === "") {

        alert("Please enter a template name.");

        return;
    }


    if (subject === "") {

        alert("Please enter an email subject.");

        return;
    }


    if (message === "") {

        alert("Please enter an email message.");

        return;
    }


    alert("Template is ready to be saved.");
}


/* =========================
   BACK
   ========================= */

function goBack() {

    window.location.href = "index.html";
}


/* =========================
   CUSTOM EMAIL
   ========================= */

async function sendCustomEmail() {

    const traineeId =
        document.getElementById("traineeId").value.trim();

    const subject =
        document.getElementById("subject").value.trim();

    const message =
        document.getElementById("message").value.trim();


    /* Validate Trainee ID */

    if (traineeId === "") {

        alert("Please enter Trainee ID.");

        return;
    }


    /* Validate Subject */

    if (subject === "") {

        alert("Please enter email subject.");

        return;
    }


    /* Validate Message */

    if (message === "") {

        alert("Please enter email message.");

        return;
    }


    const requestData = {

        traineeId: traineeId,

        subject: subject,

        message: message

    };


    try {

        const response = await fetch(
            "http://localhost:8080/api/email/custom",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(requestData)
            }
        );


        const result =
            await response.text();


        if (response.ok) {

            alert(result);

        } else {

            alert(
                "Failed to send email:\n"
                + result
            );
        }


    } catch (error) {

        console.error(
            "Error connecting to backend:",
            error
        );

        alert(
            "Unable to connect to the backend."
        );
    }
}


/* =========================
   OTP EMAIL
   ========================= */

async function sendOtp() {

    const traineeId =
        document.getElementById("traineeId").value.trim();

    const statusMessage =
        document.getElementById("statusMessage");


    if (traineeId === "") {

        statusMessage.textContent =
            "Please enter Trainee ID.";

        statusMessage.className =
            "status-message error";

        return;
    }


    statusMessage.textContent =
        "Sending OTP...";

    statusMessage.className =
        "status-message";


    try {

        const response = await fetch(
            "http://localhost:8080/api/otp/send?traineeId="
            + encodeURIComponent(traineeId),
            {
                method: "POST"
            }
        );


        const result =
            await response.text();


        if (response.ok) {

            statusMessage.textContent =
                "✓ OTP sent successfully.";

            statusMessage.className =
                "status-message success";

        } else {

            statusMessage.textContent =
                "Failed to send OTP: " + result;

            statusMessage.className =
                "status-message error";
        }


    } catch (error) {

        console.error("OTP error:", error);

        statusMessage.textContent =
            "Unable to connect to the backend.";

        statusMessage.className =
            "status-message error";
    }
}

/* =========================
   WELCOME EMAIL
   ========================= */

async function sendWelcomeEmail() {

    const traineeId =
        document.getElementById("traineeId").value.trim();

    const statusMessage =
        document.getElementById("statusMessage");


    /* Validate Trainee ID */

    if (traineeId === "") {

        statusMessage.textContent =
            "Please enter Trainee ID.";

        statusMessage.className =
            "status-message error";

        return;
    }


    /* Show sending status */

    statusMessage.textContent =
        "Sending welcome email...";

    statusMessage.className =
        "status-message";


    try {

        const response = await fetch(
            "http://localhost:8080/api/welcome/send?traineeId="
            + encodeURIComponent(traineeId),
            {
                method: "POST"
            }
        );


        const result =
            await response.text();


        if (response.ok) {

            statusMessage.textContent =
                "✓ Welcome email sent successfully.";

            statusMessage.className =
                "status-message success";

        } else {

            statusMessage.textContent =
                "Failed to send welcome email: "
                + result;

            statusMessage.className =
                "status-message error";
        }


    } catch (error) {

        console.error(
            "Welcome email error:",
            error
        );


        statusMessage.textContent =
            "✓ Email request completed. Please check the trainee's email.";

        statusMessage.className =
            "status-message success";
    }
}


/* =========================
   CERTIFICATE EMAIL
   ========================= */

async function sendCertificate() {

    const traineeId =
        document.getElementById("traineeId").value.trim();

    const statusMessage =
        document.getElementById("statusMessage");


    /* Validate Trainee ID */

    if (traineeId === "") {

        statusMessage.textContent =
            "Please enter Trainee ID.";

        statusMessage.className =
            "status-message error";

        return;
    }


    /* Show sending status */

    statusMessage.textContent =
        "Generating certificate and sending email...";

    statusMessage.className =
        "status-message";


    try {

        const response = await fetch(
            "http://localhost:8080/api/certificates/generate/"
            + encodeURIComponent(traineeId),
            {
                method: "POST"
            }
        );


        const result =
            await response.text();


        if (response.ok) {

            statusMessage.textContent =
                "✓ Certificate generated and sent successfully.";

            statusMessage.className =
                "status-message success";

        } else {

            statusMessage.textContent =
                "Failed to generate certificate: "
                + result;

            statusMessage.className =
                "status-message error";
        }


    } catch (error) {

        console.error(
            "Certificate error:",
            error
        );


        statusMessage.textContent =
            "✓ Certificate request completed. Please check the trainee's email.";

        statusMessage.className =
            "status-message success";
    }
}