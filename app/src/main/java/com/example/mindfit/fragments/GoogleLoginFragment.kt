package com.example.mindfit.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mindfit.R
import com.example.myapp.ui.activities.MainActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import androidx.fragment.app.Fragment

class GoogleLoginFragment : Fragment() {

    private val RC_SIGN_IN = 1001
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_google_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Iniciar el login con Google
        view.findViewById<View>(R.id.btn_google_sign_in).setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Éxito en el login
            Toast.makeText(requireContext(), "Bienvenido, ${account.displayName}", Toast.LENGTH_SHORT).show()

            // Redirigir al MainActivity
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Error al iniciar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
