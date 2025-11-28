-- ============================================
-- COMPLETE DATABASE EXPORT
-- ============================================

-- SCHEMA DEFINITIONS
-- ============================================

CREATE TABLE tiers(
    tier_id SERIAL PRIMARY KEY,
    tier_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    total_points INT DEFAULT 0,
    tier_id INT REFERENCES tiers(tier_id)
);

CREATE TABLE events(
    event_id SERIAL PRIMARY KEY,
    creator_id INT REFERENCES users(user_id),
    title VARCHAR(200) NOT NULL,
    event_date DATE NOT NULL,
    reward INT NOT NULL,
    description TEXT
);

CREATE TABLE bets(
    bet_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    creator_id INT REFERENCES users(user_id),
    target_id INT REFERENCES users(user_id),
    wager_points INT NOT NULL,
    bet_details TEXT,
    actual_outcome BOOLEAN,
    payout_points INT,
    loss_points INT,
    resolution_date DATE
);

CREATE TABLE logs(
    log_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    details VARCHAR(100),
    log_time TIMESTAMP DEFAULT NOW()
);

-- DATA INSERTS
-- ============================================

-- TIERS
INSERT INTO tiers (tier_name) VALUES 
('dreg'),
('citizen'),
('elite');

-- USERS
INSERT INTO users (username, password_hash, total_points, tier_id) VALUES
('CryptoWhale69', '1234', 125000, 3),
('KarenFromHR', '1234', 84200, 3),
('BigDave', '1234', 76900, 3),
('SilentSniper', '1234', 45300, 2),
('TaxEvader420', '1234', 42100, 2),
('ExcelNinja', '1234', 39800, 2),
('CoffeeAddict', '1234', 35700, 2),
('PPT_God', '1234', 31200, 2),
('ZoomBackgroundPro', '1234', 28900, 2),
('LayoffSurvivor', '1234', 8780, 1),
('InternFromHell', '1234', 8200, 1),
('ReplyAllOffender', '1234', 6300, 1),
('OutlookWarrior', '1234', 4100, 1),
('PrinterKiller', '1234', 1900, 1),
('MuteButtonDenier', '1234', 800, 1);

-- EVENTS (negative rewards)
INSERT INTO events (creator_id, title, event_date, reward, description) VALUES
(1, 'Someone says "synergy" unironically in the All-Hands', '2025-12-05', -5000, 'Must be audible to at least 50 people. Bonus -2000 points if said by C-level.'),
(2, 'HR sends a wellness email the same week layoffs are announced', '2025-12-20', -8000, 'Screenshot required. Double points if it mentions "resilience".'),
(3, 'A manager uses the phrase "do more with less" in 1:1', '2025-11-28', -3500, 'Recording or witness signature required.'),
(4, 'Fire alarm goes off and nobody leaves their desk', '2026-01-15', -10000, 'Video evidence mandatory. Bonus if someone yells "It''s just a drill!".'),
(5, 'Someone cries in the bathroom (caught on audio)', '2025-12-12', -6000, 'Discreet recording only. Identity protected.'),
(6, 'The coffee machine dies permanently', '2025-12-18', -7000, 'Must remain broken >5 business days. Funeral service encouraged.'),
(7, 'CEO posts LinkedIn thought-leadership about "work-life balance"', '2025-12-24', -9000, 'While simultaneously sending 10pm emails. Screenshot combo = 1.5x multiplier.'),
(8, 'Mandatory fun team-building event announced', '2026-01-08', -4500, 'Extra -3000 if it''s trust falls or escape room.'),
(9, 'Someone gets promoted who started after you', '2026-02-01', -12000, 'Org chart proof required.'),
(10, 'IT sends company-wide email about phishing... that is itself a phishing test', '2025-12-10', -6500, 'Double points if >20 people fail.'),
(11, 'The office plants finally die from neglect', '2026-01-20', -4000, 'Time-lapse encouraged.'),
(12, 'A senior leader says "We''re a family" right before RIF', '2025-12-16', -15000, 'Highest single payout event of Q4.'),
(1, 'Someone brings a baby to "Take Your Child to Work Day" and it screams through a client call', '2026-04-01', -5500, 'Audio proof required.'),
(4, 'The intern is asked to book the holiday party at the same venue as last year''s layoffs', '2025-12-03', -7500, 'Booking confirmation screenshot.'),
(6, 'The thermostat war reaches actual physical sabotage', '2026-01-30', -11000, 'Evidence of tampering required. No injuries.');

-- BETS (only won bets with future resolution dates)
INSERT INTO bets (user_id, creator_id, target_id, wager_points, bet_details, actual_outcome, payout_points, loss_points, resolution_date) VALUES
(4, 1, 10, 5000, 'LayoffSurvivor mentions "quiet quitting" in standup before Christmas', true, 8000, -5000, '2026-01-20'),
(7, 2, 7, 3000, 'CoffeeAddict will microwave fish this month', true, 4800, -3000, '2026-02-15'),
(9, 4, 2, 7000, 'KarenFromHR uses the crying emoji in Slack about deadlines', true, 11200, -7000, '2025-11-28'),
(13, 8, 13, 1500, 'OutlookWarrior replies-all to a 500+ recipient thread', true, 2400, -1500, '2025-11-10'),
(2, 1, 15, 10000, 'MuteButtonDenier gets muted by host >3 times in one town hall', true, 16000, -10000, '2026-01-15'),
(3, 2, 12, 6000, 'ReplyAllOffender hits "Reply All" on a calendar invite', true, 9600, -6000, '2025-11-22'),
(14, 3, 6, 4500, 'ExcelNinja admits to using mouse instead of shortcuts', true, 7200, -4500, '2026-01-25'),
(15, 1, 13, 800, 'OutlookWarrior''s signature still says "Sent from my iPhone" on desktop', true, 1280, -800, '2026-02-10'),
(1, 9, 3, 12000, 'BigDave gets caught using ChatGPT for performance reviews', true, 19200, -12000, '2026-03-25');

-- LOGS
INSERT INTO logs (user_id, details, log_time) VALUES
-- Initial logs
(1, 'Successful login from 192.168.1.20', '2025-11-20 09:15:00'),
(2, 'Bet ID 8: Zuckerburg pulls another tech demo fail', '2025-11-21 14:30:00'),
(1, 'Changed tier to elite', '2025-11-21 16:45:00'),
(3, 'Joined Unhinged Stunt Gala', '2025-11-22 10:00:00'),

-- ELITE tier activities
(1, 'Accessed executive surveillance dashboard', '2025-11-15 08:30:00'),
(1, 'Reviewed quarterly termination candidates list', '2025-11-15 10:15:00'),
(1, 'Approved budget cut proposal for IT department', '2025-11-15 14:20:00'),
(1, 'Placed bet: CFO will announce layoffs before holidays', '2025-11-16 09:45:00'),
(1, 'Accessed restricted salary database', '2025-11-16 11:30:00'),
(1, 'Reported employee #247 for "insufficient enthusiasm"', '2025-11-16 15:00:00'),
(1, 'Attended closed-door restructuring meeting', '2025-11-17 08:00:00'),
(1, 'Flagged 5 employees for performance review', '2025-11-17 13:25:00'),
(2, 'Reported 3 citizens for productivity violations', '2025-11-16 11:20:00'),
(2, 'Approved expense report: $5000 team dinner', '2025-11-19 16:30:00'),
(2, 'Fast-tracked promotion bypass approval', '2025-11-22 10:45:00'),
(3, 'Claimed VIP parking spot allocation', '2025-11-17 07:45:00'),
(3, 'Placed high-stakes bet on quarterly layoffs', '2025-11-20 13:15:00'),
(3, 'Accessed restricted HR termination database', '2025-11-23 15:20:00'),

-- CITIZEN tier activities
(4, 'Submitted weekly status report on time', '2025-11-15 17:00:00'),
(4, 'Attended team standup meeting', '2025-11-21 10:00:00'),
(5, 'Joined mandatory compliance training', '2025-11-16 10:30:00'),
(5, 'Filed expense report: $42 parking', '2025-11-22 14:15:00'),
(6, 'Created 47 pivot tables in single Excel session', '2025-11-17 12:00:00'),
(6, 'Completed keyboard shortcut certification', '2025-11-23 16:00:00'),
(7, 'Coffee machine usage: 8 cups today', '2025-11-18 15:45:00'),
(7, 'Requested additional coffee budget approval', '2025-11-23 08:30:00'),
(8, 'PowerPoint deck approved by manager', '2025-11-19 11:30:00'),
(9, 'Changed Zoom background to corporate logo', '2025-11-20 09:20:00'),

-- DREG tier activities
(10, 'Failed to login 3 times - account flagged', '2025-11-15 09:45:00'),
(10, 'Missed mandatory sensitivity training deadline', '2025-11-21 17:00:00'),
(11, 'Submitted intern expense: suspicious charges', '2025-11-16 13:20:00'),
(11, 'Flagged for suspicious web browsing history', '2025-11-22 12:45:00'),
(12, 'Reply-all incident reported by 47 colleagues', '2025-11-17 11:10:00'),
(12, 'Email etiquette violation: 4th offense this month', '2025-11-23 09:30:00'),
(13, 'Outlook signature violation detected', '2025-11-18 14:50:00'),
(13, 'Desk audit failed: unauthorized personal items', '2025-11-23 13:20:00'),
(14, 'Printer jam caused by unauthorized paper size', '2025-11-19 10:15:00'),
(14, 'IT ticket: "Printer won''t stop making grinding noise"', '2025-11-23 16:45:00'),
(15, 'Muted by host in all-hands meeting', '2025-11-20 15:30:00'),
(15, 'Zoom meeting participation: 0% - camera off entire call', '2025-11-23 11:00:00');