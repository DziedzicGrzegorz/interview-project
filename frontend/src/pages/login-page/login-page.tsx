import {useForm, SubmitHandler} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import {Button} from "@/components/ui/button/button.tsx";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {
    Tabs,
    TabsContent,
    TabsList,
    TabsTrigger,
} from "@/components/ui/tabs";
import {useCheckAuthStatus} from "@/hooks/use-login-status";
import {SkeletonDemo} from "@/components/loading";
import {Navigate, useNavigate} from "react-router-dom";
import {Axios} from "@/utils/Axios.ts";
import {useEffect, useState} from "react";
import {useToast} from "@/components/ui/use-toast.ts";
import {ToastAction} from "@radix-ui/react-toast";
import {ErrorMessage} from "@/components/error-message.tsx";
import {loginSchema, registerSchema} from "@/pages/login-page/util/login-pages.ts";
import {ErrorState, FormFields, LoginFormFields, RegisterFormFields} from "@/pages/login-page/util/login-page";


export function LoginPage() {
    const userIsLogged = useCheckAuthStatus();
    const navigate = useNavigate();
    //to catch login errors from the backend
    const [loginError, setLoginError] = useState<ErrorState | null>(null);
    const [registerError, setRegisterError] = useState<ErrorState | null>(null);
    const {toast} = useToast();

    useEffect(() => {
        if (loginError) {
            toast({
                variant: "destructive",
                title: loginError?.title,
                description: loginError?.description,
                action: <ToastAction altText="Close" onClick={() => setLoginError(null)}>Close</ToastAction>
            });
        }

        if (registerError) {
            toast({
                variant: "destructive",
                title: registerError.title,
                description: registerError.description,
                action: <ToastAction altText="Close" onClick={() => setRegisterError(null)}>Close</ToastAction>,
            });
        }
    }, [loginError, registerError, toast]);
    const {
        register: registerLogin,
        handleSubmit: handleSubmitLogin,
        formState: {errors: errorsLogin}
    } = useForm<LoginFormFields>({
        resolver: zodResolver(loginSchema),
    });

    const {
        register: registerRegister,
        handleSubmit: handleSubmitRegister,
        formState: {errors: errorsRegister},
    } = useForm<RegisterFormFields>({
        resolver: zodResolver(registerSchema),
    });
    const handleAuthProcess = async (
        endpoint: string,
        data: FormFields,
        navigateTo: string,
        setError: (errorState: ErrorState) => void
    ) => {
        try {
            await Axios.post(endpoint, data);
            navigate(navigateTo);
        } catch (error) {
            if (Axios.isAxiosError(error)) {
                // Specify the type of the error response data here
                const errorResponse = error.response?.data as ErrorResponse;
                const errorMessage = errorResponse.message || errorResponse.password || errorResponse.error || "An unknown error occurred.";
                setError({title: "Error", description: errorMessage});
            } else {
                // Handle non-Axios errors
                setError({title: "Error", description: "An unknown error occurred."});
            }
        }
    }
    const onSubmitLogin: SubmitHandler<LoginFormFields> = (data) => {
        handleAuthProcess('/auth/login', data, '/dashboard', setLoginError);
    };

    const onSubmitRegister: SubmitHandler<RegisterFormFields> = (data) => {
        handleAuthProcess('auth/register', data, '/dashboard', setRegisterError);
    };

    if (userIsLogged) {
        return <Navigate to="/dashboard" replace/>
    }

    if (userIsLogged === null) {
        return <SkeletonDemo/>;
    }
    return (
        <div className="h-screen flex items-center justify-center">
            <Tabs defaultValue="login" className="w-[400px] m-5">
                <TabsList className="grid w-full grid-cols-2 ">
                    <TabsTrigger value="login">Login</TabsTrigger>
                    <TabsTrigger value="register">Register</TabsTrigger>
                </TabsList>
                <div className="min-h-[31rem] pt-5">
                    <TabsContent value="login">
                        <Card>
                            <form onSubmit={handleSubmitLogin(onSubmitLogin)} noValidate>
                                <CardHeader>
                                    <CardTitle>Login</CardTitle>
                                    <CardDescription>
                                        Login to the system
                                    </CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-2">
                                    <div className="space-y-1">
                                        <Label htmlFor="email">Email</Label>
                                        <Input id="email" type="email" placeholder="m@example.com"
                                               {...registerLogin('email')}
                                        />
                                        <ErrorMessage error={errorsLogin.email}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="username">Password</Label>
                                        <Input id="username" type="password"
                                               {...registerLogin('password')}
                                        />
                                        <ErrorMessage error={errorsLogin.password}/>
                                    </div>
                                </CardContent>
                                <CardFooter>
                                    <Button>Login</Button>
                                </CardFooter>
                            </form>
                        </Card>
                    </TabsContent>
                    <TabsContent value="register">
                        <form onSubmit={handleSubmitRegister(onSubmitRegister)} noValidate>
                            <Card>
                                <CardHeader>
                                    <CardTitle>Register</CardTitle>
                                    <CardDescription>
                                        Create an account in the system
                                    </CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-2">
                                    <div className="space-y-1">
                                        <Label htmlFor="registerEmail">Email</Label>
                                        <Input id="registerEmail" type="email" placeholder="m@example.com"
                                               {...registerRegister('email')}
                                        />
                                        <ErrorMessage error={errorsRegister.email}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="registerUsername">Username</Label>
                                        <Input id="registerUsername" type="text" placeholder="Your Name"
                                               {...registerRegister('username')}
                                        />
                                        <ErrorMessage error={errorsRegister.username}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="registerPassword">Password</Label>
                                        <Input id="registerPassword" type="password"
                                               {...registerRegister('password')}
                                        />
                                        <ErrorMessage error={errorsRegister.password}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="repeatPassword">Repeat Password</Label>
                                        <Input id="repeatPassword" type="password"
                                               {...registerRegister("repeatPassword")}
                                        />
                                        <ErrorMessage error={errorsRegister.repeatPassword}/>
                                    </div>
                                </CardContent>
                                <CardFooter>
                                    <Button type="submit">Register</Button>
                                </CardFooter>
                            </Card>
                        </form>
                    </TabsContent>
                </div>
            </Tabs>
        </div>
    )
}
