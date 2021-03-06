package apap.tugasakhir.situ.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure (WebSecurity web)  {
        web.ignoring().antMatchers("/api/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/daftar-pengajuan-surat").hasAnyAuthority(("Admin TU"), ("Guru"), ("Siswa") , ("Kepala Sekolah"))
                .antMatchers("/pengajuan-surat/tambah").hasAnyAuthority(("Admin TU"), ("Guru"), ("Siswa"))
                .antMatchers("/pengajuan-surat/ubah/**").hasAnyAuthority(("Admin TU"), ("Kepala Sekolah"))
                .antMatchers("/pengajuan-surat/**").hasAnyAuthority(("Admin TU"), ("Guru"), ("Siswa"))
                .antMatchers("/lowongan/**").hasAnyAuthority(("Admin TU"))
                .antMatchers("/jenis-surat/**").hasAnyAuthority(("Admin TU"))
                .antMatchers("/jenis-lowongan/**").hasAnyAuthority(("Admin TU"))
                .antMatchers("/pengajuan-peminjaman").hasAnyAuthority(("Admin TU"))
                .antMatchers("/user/add-employee").hasAnyAuthority(("Admin TU"))
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login").permitAll();
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
}

