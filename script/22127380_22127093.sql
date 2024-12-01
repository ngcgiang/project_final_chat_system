-- Tạo cơ sở dữ liệu
CREATE DATABASE chat_system;
USE chat_system;

-- Bảng lưu thông tin người dùng
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã người dùng
    Username VARCHAR(50) UNIQUE NOT NULL,  -- Tên đăng nhập
    PasswordHash VARCHAR(255) NOT NULL,  -- Mật khẩu đã mã hóa
    Email VARCHAR(100) UNIQUE NOT NULL,  -- Địa chỉ email
    FullName VARCHAR(100),  -- Tên đầy đủ
    Status ENUM('online', 'offline') DEFAULT 'offline',  -- Trạng thái người dùng
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Thời gian tạo tài khoản
);

-- Bảng lưu thông tin quản trị viên
CREATE TABLE Administrators (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã quản trị viên
    UserID INT NOT NULL,  -- Liên kết với người dùng (User)
    AssignedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian phân công làm quản trị viên
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE  -- Liên kết với bảng Users
);

-- Bảng lưu danh sách người dùng bị chặn
CREATE TABLE BlockList (
    BlockID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã chặn
    BlockerID INT NOT NULL,  -- Người chặn
    BlockedID INT NOT NULL,  -- Người bị chặn
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian chặn
    FOREIGN KEY (BlockerID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (BlockedID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu danh sách bạn bè
CREATE TABLE Friends (
    ID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã bạn bè
    UserID INT NOT NULL,  -- ID người dùng
    FriendUserID INT NOT NULL,  -- ID bạn bè (thay đổi tên từ FriendID)
    IsBlocked BOOLEAN DEFAULT FALSE,  -- Trạng thái bị chặn
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian kết bạn
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (FriendUserID) REFERENCES Users(UserID) ON DELETE CASCADE  -- Liên kết với bảng Users
);

-- Bảng lưu các yêu cầu kết bạn
CREATE TABLE FriendRequests (
    RequestID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã yêu cầu kết bạn
    SenderID INT NOT NULL,  -- Người gửi yêu cầu
    ReceiverID INT NOT NULL,  -- Người nhận yêu cầu
    Status ENUM('pending', 'accepted', 'declined') DEFAULT 'pending',  -- Trạng thái yêu cầu
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi yêu cầu
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu tin nhắn cá nhân
CREATE TABLE Messages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã tin nhắn
    SenderID INT NOT NULL,  -- Người gửi
    ReceiverID INT NOT NULL,  -- Người nhận
    Content TEXT NOT NULL,  -- Nội dung tin nhắn
    SentAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi
    IsRead BOOLEAN DEFAULT FALSE,  -- Trạng thái đã đọc
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng thông tin nhóm
CREATE TABLE GroupInfo (
    GroupID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã nhóm
    GroupName VARCHAR(100) NOT NULL,  -- Tên nhóm
    AdminID INT NOT NULL,  -- ID người quản trị nhóm
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tạo nhóm
    FOREIGN KEY (AdminID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng thành viên nhóm
CREATE TABLE GroupMembers (
    MemberID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã thành viên
    GroupID INT NOT NULL,  -- Mã nhóm
    UserID INT NOT NULL,  -- Mã người dùng
    IsAdmin BOOLEAN DEFAULT FALSE,  -- Quản trị viên nhóm
    AddedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tham gia nhóm
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng tin nhắn nhóm
CREATE TABLE GroupMessages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã tin nhắn nhóm
    GroupID INT NOT NULL,  -- Mã nhóm
    SenderID INT NOT NULL,  -- Người gửi
    Content TEXT NOT NULL,  -- Nội dung tin nhắn
    SentAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID) ON DELETE CASCADE,
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng báo cáo spam
CREATE TABLE SpamReports (
    ReportID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã báo cáo spam
    UserID INT NOT NULL,  -- Mã người bị báo cáo
    ReportedBy INT NOT NULL,  -- Mã người báo cáo
    ReportTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian báo cáo
    Reason TEXT NOT NULL,  -- Lý do báo cáo
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (ReportedBy) REFERENCES Users(UserID)
);

-- Bảng hoạt động của người dùng
CREATE TABLE UserActivities (
    ActivityID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã hoạt động
    UserID INT NOT NULL,  -- Mã người dùng
    ActivityType VARCHAR(50),  -- Loại hoạt động (Login, Message, ...)
    LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian đăng nhập
    LogoutTime TIMESTAMP NULL,  -- Thời gian đăng xuất (nếu có)
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng đăng ký mới
CREATE TABLE NewRegistrations (
    RegistrationID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã đăng ký mới
    UserID INT NOT NULL,  -- Mã người dùng
    RegistrationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian đăng ký
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng nhóm (có thể chứa thông tin nhóm khác)
CREATE TABLE UserGroups (
    GroupID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã nhóm
    NameGroup VARCHAR(100) NOT NULL,  -- Tên nhóm
    Admin INT NOT NULL,  -- Người quản trị nhóm
    CreationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tạo nhóm
    AmountOfMember INT DEFAULT 0,  -- Số lượng thành viên nhóm
    FOREIGN KEY (Admin) REFERENCES Users(UserID)
);

-- Bảng thành viên tham gia nhóm
CREATE TABLE UserParticipateGroups (
    UserID INT NOT NULL,  -- Mã người dùng
    GroupID INT NOT NULL,  -- Mã nhóm
    ParticipationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tham gia
    PRIMARY KEY (UserID, GroupID),  -- Đảm bảo mỗi người chỉ tham gia nhóm một lần
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (GroupID) REFERENCES UserGroups(GroupID)
);

-- Bảng lịch sử trò chuyện
CREATE TABLE ChatHistory (
    ChatHistoryID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã lịch sử trò chuyện
    UserID INT NOT NULL,  -- Mã người dùng
    GroupID INT DEFAULT NULL,  -- Mã nhóm (nếu có)
    MessageContent TEXT NOT NULL,  -- Nội dung tin nhắn
    MessageTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi tin nhắn
    IsGroupMessage BOOLEAN DEFAULT FALSE,  -- Tin nhắn nhóm hay cá nhân
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (GroupID) REFERENCES UserGroups(GroupID)
);

