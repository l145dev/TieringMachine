import React, { useEffect, useState, useRef } from 'react';
import { Box, Typography, keyframes } from '@mui/material';
import alarmSound from '../assets/super-loud-ahh-alarm-165805.mp3';

const flash = keyframes`
  0% { background-color: red; }
  50% { background-color: darkred; }
  100% { background-color: red; }
`;

const TerminationAlarm: React.FC = () => {
    const [alarmTriggered, setAlarmTriggered] = useState(false);
    const audioRef = useRef<HTMLAudioElement | null>(null);

    useEffect(() => {
        const handleKeydown = (event: KeyboardEvent) => {
            if (event.key === 'Enter') {
                setAlarmTriggered(true);
                playAlarmSound();
            } else if (event.key === 'Escape' && alarmTriggered) {
                setAlarmTriggered(false);
                stopAlarmSound();
            }
        };

        document.addEventListener('keydown', handleKeydown);

        return () => {
            document.removeEventListener('keydown', handleKeydown);
        };
    }, [alarmTriggered]);

    const playAlarmSound = () => {
        if (!audioRef.current) {
            audioRef.current = new Audio(alarmSound);
            audioRef.current.loop = true;
        }
        audioRef.current.play().catch(e => console.error("Audio play failed:", e));
    };

    const stopAlarmSound = () => {
        if (audioRef.current) {
            audioRef.current.pause();
            audioRef.current.currentTime = 0;
        }
    };

    if (!alarmTriggered) return null;

    return (
        <Box
            sx={{
                position: 'fixed',
                top: 0,
                left: 0,
                width: '100vw',
                height: '100vh',
                zIndex: 9999,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                animation: `${flash} 0.5s infinite`,
                cursor: 'none',
            }}
        >
            <Typography
                variant="h1"
                align="center"
                sx={{
                    color: 'white',
                    fontWeight: 'bold',
                    textTransform: 'uppercase',
                    textShadow: '0 0 10px black',
                    padding: 4,
                    fontSize: '4rem',
                }}
            >
                You have not finished your job. termination is immediate
            </Typography>
        </Box>
    );
};

export default TerminationAlarm;
